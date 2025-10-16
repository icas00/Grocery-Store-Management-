package grocery.service;

import grocery.entity.Product;
import grocery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repo;

    public Product add(Product p){ return repo.save(p); }
    public List<Product> all(){ return repo.findAll(); }
    public Product get(Long id){ return repo.findById(id).orElseThrow(() -> new RuntimeException("Not found")); }
    public Product update(Long id, Product req){
        Product p = get(id);
        p.setName(req.getName()); p.setPrice(req.getPrice()); p.setQuantity(req.getQuantity());
        p.setCategory(req.getCategory()); p.setDescription(req.getDescription());
        return repo.save(p);
    }
    public void delete(Long id){ repo.deleteById(id); }
}
