package arwa.Services;

import arwa.DTO.OrderItemDto;
import arwa.Entities.OrderItem;
import arwa.Repository.OrderItemRepository;
import arwa.Repository.OrderRepository;
import arwa.Repository.ProductRepository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j

@Transactional
public class OrderItemService {


    OrderItemRepository orderItemRepository;


    OrderRepository orderRepository;


    ProductRepository productRepository;

    public static OrderItemDto mapToDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getProduct().getId(),
                orderItem.getOrder().getId()
        );
    }

    public OrderItemDto findById(Long id) {
        log.debug("Request to get OrderItem : {}", id);
        return this.orderItemRepository.findById(id).map(OrderItemService::mapToDto).orElse(null);
    }

    public OrderItemDto create(OrderItemDto orderItemDto) {
        log.debug("Request to create OrderItem : {}", orderItemDto);
        var order =
                this.orderRepository
                        .findById(orderItemDto.getOrderId())
                        .orElseThrow(() -> new IllegalStateException("The Order does not exist!"));

        var product =
                this.productRepository
                        .findById(orderItemDto.getProductId())
                        .orElseThrow(() -> new IllegalStateException("The Product does not exist!"));

        var orderItem = this.orderItemRepository.save(
                new OrderItem(
                        orderItemDto.getQuantity(),
                        product,
                        order
                ));

        order.setPrice(order.getPrice().add(orderItem.getProduct().getPrice()));

        this.orderRepository.save(order);

        return mapToDto(orderItem);
    }

    public void delete(Long id) {
        log.debug("Request to delete OrderItem : {}", id);

        var orderItem = this.orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("The OrderItem does not exist!"));

        var order = orderItem.getOrder();
        order.setPrice(
                order.getPrice().subtract(orderItem.getProduct().getPrice())
        );

        this.orderItemRepository.deleteById(id);

        order.getOrderItems().remove(orderItem);

        this.orderRepository.save(order);
    }

    public List<OrderItemDto> findByOrderId(Long id) {
        log.debug("Request to get all OrderItems of OrderId {}", id);
        return this.orderItemRepository.findAllByOrderId(id)
                .stream()
                .map(OrderItemService::mapToDto)
                .collect(Collectors.toList());
    }
}