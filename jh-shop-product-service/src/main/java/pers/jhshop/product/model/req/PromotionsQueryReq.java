package pers.jhshop.product.model.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.entity.Promotions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品促销表查询Req
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PromotionsQueryReq", description = "商品促销表查询Req")
public class PromotionsQueryReq extends Page<Promotions> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "促销唯一标识")
    private Long id;

    @ApiModelProperty(value = "商品ID，外键关联PRODUCTS表")
    private Integer productId;

    @ApiModelProperty(value = "促销类型（如：打折、满减等）")
    private String promotionType;

    @ApiModelProperty(value = "促销类型（如：打折、满减等）-模糊匹配")
    private String promotionTypeLike;

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

    @ApiModelProperty(value = "描述-模糊匹配")
    private String descriptionLike;

    @ApiModelProperty(value = "生效标志(TRUE-生效, FALSE-失效)")
    private Boolean validFlag;



}
