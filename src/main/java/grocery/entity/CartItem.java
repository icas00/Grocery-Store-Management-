package grocery.entity;

import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {
    private Long productId;
    private int quantity;
}
