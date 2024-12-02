package pers.jhshop.product.model.entity;

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
 * 商品评价表
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_reviews")
@ApiModel(value = "Reviews对象", description = "商品评价表")
public class Reviews extends Model<Reviews> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评价唯一标识")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品ID，外键关联PRODUCTS表")
    @TableField("PRODUCT_ID")
    private Integer productId;

    @ApiModelProperty(value = "用户ID，外键关联USERS表")
    @TableField("USER_ID")
    private Integer userId;

    @ApiModelProperty(value = "评分（1-5）")
    @TableField("RATING")
    private Integer rating;

    @ApiModelProperty(value = "评价内容")
    @TableField("COMMENT")
    private String comment;

    @ApiModelProperty(value = "评价时间")
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
