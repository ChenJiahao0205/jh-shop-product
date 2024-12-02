package pers.jhshop.product.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品促销表
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_promotions")
@ApiModel(value = "Promotions对象", description = "商品促销表")
public class Promotions extends Model<Promotions> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "促销唯一标识")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品ID，外键关联PRODUCTS表")
    @TableField("PRODUCT_ID")
    private Integer productId;

    @ApiModelProperty(value = "促销类型（如：打折、满减等）")
    @TableField("PROMOTION_TYPE")
    private String promotionType;

    @ApiModelProperty(value = "折扣金额或折扣百分比")
    @TableField("DISCOUNT")
    private BigDecimal discount;

    @ApiModelProperty(value = "促销开始时间")
    @TableField("START_TIME")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "促销结束时间")
    @TableField("END_TIME")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "促销创建时间")
    @TableField("CREATED_AT")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "生效标志(TRUE-生效, FALSE-失效)")
    @TableField("VALID_FLAG")
    private Boolean validFlag;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
