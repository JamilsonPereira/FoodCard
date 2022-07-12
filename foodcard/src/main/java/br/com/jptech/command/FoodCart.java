package br.com.jptech.command;

import br.com.jptech.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aggregate
public class FoodCart {
    private static final Logger log = LoggerFactory.getLogger(FoodCart.class);

    @AggregateIdentifier
    private UUID foodCardId;
    private Map<UUID, Integer> selectedProduct;
    private boolean confirmed;


    @CommandHandler
    public FoodCart(CreateFoodCartCommand command){
        //Interface do Axon que busca o ciclo de vida do agregado, chamando o evento respectivo dele
        AggregateLifecycle.apply(new FoodCartCreateEvent(command.getFoodCartId()));
    }

    @CommandHandler
    public void handle(SelectProductCommand command){
        AggregateLifecycle.apply(new ProductSelectedEvent(foodCardId, command.getProductId(), command.getQuantity()));
    }

    @CommandHandler
    public void handle(DeselectProductCommand command){
        AggregateLifecycle.apply(new ProductDeselectedEvent(foodCardId, command.getProductId(), command.getQuantity()));
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command){
        AggregateLifecycle.apply(new OrderConfirmedEvent(foodCardId));
    }

    @EventSourcingHandler
    public void on(FoodCartCreateEvent event){
        foodCardId = event.getFoodCartId();
        selectedProduct = new HashMap<>();
        confirmed = false;
    }

    @EventSourcingHandler
    public void on(ProductSelectedEvent event){
        selectedProduct.merge(event.getProductId(), event.getQuantity(), Integer::sum);
    }

    @EventSourcingHandler
    public void on(ProductDeselectedEvent event){
        selectedProduct.computeIfPresent(
                event.getProductId(),
                (productId, quantity) -> quantity -= event.getQuantity());
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event){
        confirmed = true;
    }

    //construtor vazio pois o Axon precisa de um construtor vazio
    public FoodCart(){

    }
}
