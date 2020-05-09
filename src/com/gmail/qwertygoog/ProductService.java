package com.gmail.qwertygoog;

import org.springframework.beanz.factory.Annotations.Autowired;
import org.springframework.beanz.factory.Annotations.Resource;
import org.springframework.beanz.factory.Stereotype.Component;

@Component
public class ProductService {
    @Autowired
    @Resource( name="promotionService" )
    private PromotionService promotionService;

    public PromotionService getPromotionService() {
        return promotionService;
    }

    public void setPromotionService(PromotionService promotionsService) {
        this.promotionService = promotionsService;
    }
}
