package grocery.controller;

import grocery.entity.Product;
import grocery.repository.ProductRepository;
import grocery.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository repo;
    private final JwtUtil jwtUtil;

    @GetMapping
    public List<Product> all(){ return repo.findAll(); }

    @PostMapping
    public ResponseEntity<?> add(@RequestHeader(value="Authorization", required=false) String auth,
                                 @RequestBody Product p){

        if(auth==null || !auth.startsWith("Bearer ")) return ResponseEntity.status(401).body("login required");
        String token = auth.substring(7);
        if(!jwtUtil.validate(token)) return ResponseEntity.status(401).body("invalid token");
        if(!jwtUtil.extractRoles(token).contains("ROLE_ADMIN")) return ResponseEntity.status(403).body("admin only");
        Product saved = repo.save(p);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        Optional<Product> p = repo.findById(id);
        if(p.isEmpty()) return ResponseEntity.status(404).body("not found");
        return ResponseEntity.ok(p.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader(value="Authorization", required=false) String auth,
                                    @PathVariable Long id, @RequestBody Product req){
        
        if(auth==null || !auth.startsWith("Bearer ")) return ResponseEntity.status(401).body("login required");
        String token = auth.substring(7); if(!jwtUtil.validate(token)) return ResponseEntity.status(401).body("invalid token");
        if(!jwtUtil.extractRoles(token).contains("ROLE_ADMIN")) return ResponseEntity.status(403).body("admin only");
        Product p = repo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        p.setName(req.getName()); p.setPrice(req.getPrice()); p.setQuantity(req.getQuantity());
        p.setDescription(req.getDescription()); p.setCategory(req.getCategory());
        repo.save(p);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader(value="Authorization", required=false) String auth,
                                    @PathVariable Long id){
        if(auth==null || !auth.startsWith("Bearer ")) return ResponseEntity.status(401).body("login required");
        String token = auth.substring(7); if(!jwtUtil.validate(token)) return ResponseEntity.status(401).body("invalid token");
        if(!jwtUtil.extractRoles(token).contains("ROLE_ADMIN")) return ResponseEntity.status(403).body("admin only");
        repo.deleteById(id); return ResponseEntity.ok(Map.of("message","deleted"));
    }
}
// temporary log added for testing
