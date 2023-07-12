package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ItemInterface;
import com.adobe.aem.guides.wknd.core.models.Properties;
import com.adobe.aem.guides.wknd.core.models.SellOfferItem;
import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Model(
        adaptables = {Resource.class},
        adapters = {SellOfferItem.class},
        resourceType = {SellOfferItemImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SellOfferItemImpl implements SellOfferItem {

    protected static final String RESOURCE_TYPE = "wknd/components/sellofferitem";

    @Self
    private FridgeModel fridgeModel;

    @Self
    private TvModel tvModel;

    @ValueMapValue(name = "productImageRef")
    private String productImageRef;

    @ValueMapValue(name = "product")
    private String product;

    @ValueMapValue(name = "valueofdiscount")
    private Integer discount;

    @ValueMapValue(name = "specialoffer")
    private String specialoffer;

    private ItemInterface currentModel;

    @PostConstruct
    public void init() {
        currentModel = (product != null && product.equals("fridge")) ? fridgeModel : tvModel;
    }

    @Override
    public String getImage() {
        return productImageRef;
    }

    @Override
    public boolean isSpecialOffer() {
        return specialoffer.equals("Yes");
    }

    public String getProduct() {
        return product;
    }

    @Override
    public List<Properties> getProperties() {
        return currentModel.getProperties();
    }

    @Override
    public Double getPrice() {
        return getTotalPrice(currentModel.getPrice());
    }

    public Integer getDiscount() {
        return discount;
    }

    @Override
    public Double getTotalPrice(BigDecimal price) {
        if (isSpecialOffer()) {
            return price.subtract(
                            price.multiply(
                                    BigDecimal.valueOf(getDiscount())).divide(
                                    BigDecimal.valueOf(100), 4, RoundingMode.HALF_DOWN))
                    .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
        }
        return price.doubleValue();
    }

    public String getNameOfProduct() {
        return currentModel.getProducer() + " " + currentModel.getModel();
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public void setSpecialoffer(String specialoffer) {
        this.specialoffer = specialoffer;
    }
}
