package pers.jhshop.product.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 商品表
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("products")
@ApiModel(value = "Products对象", description = "商品表")
public class Products extends Model<Products> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品唯一标识")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "商品描述")
    @TableField("PRODUCT_DESCRIPTION")
    private String productDescription;

    @ApiModelProperty(value = "商品品牌")
    @TableField("BRAND")
    private String brand;

    @ApiModelProperty(value = "商品分类ID，外键关联CATEGORIES表")
    @TableField("CATEGORY_ID")
    private Integer categoryId;

    @ApiModelProperty(value = "商品价格")
    @TableField("PRICE")
    private BigDecimal price;

    @ApiModelProperty(value = "商品库存")
    @TableField("STOCK")
    private Integer stock;

    @ApiModelProperty(value = "商品状态（1=在售，0=下架）")
    @TableField("STATUS")
    private Boolean status;

    @ApiModelProperty(value = "商品创建时间")
    @TableField("CREATED_AT")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "商品信息最后更新时间")
    @TableField("UPDATED_AT")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "商品主图URL")
    @TableField("IMAGE_URL")
    private String imageUrl;

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
