package pers.jhshop.product.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import pers.jhshop.common.entity.BaseVo;

/**
 * <p>
 * 商品促销表VO
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PromotionsVO", description = "商品促销表列表展示VO")
public class PromotionsVO extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "促销唯一标识")
    private Long id;

    @ApiModelProperty(value = "商品ID，外键关联PRODUCTS表")
    private Integer productId;

    @ApiModelProperty(value = "促销类型（如：打折、满减等）")
    private String promotionType;

    @ApiModelProperty(value = "折扣金额或折扣百分比")
    private BigDecimal discount;

    @ApiModelProperty(value = "促销开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "促销结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "促销创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "描述")
    private String description;

}
