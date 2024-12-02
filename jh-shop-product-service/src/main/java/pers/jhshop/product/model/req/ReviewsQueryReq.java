package pers.jhshop.product.model.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.entity.Reviews;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品评价表查询Req
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ReviewsQueryReq", description = "商品评价表查询Req")
public class ReviewsQueryReq extends Page<Reviews> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评价唯一标识")
    private Long id;

    @ApiModelProperty(value = "商品ID，外键关联PRODUCTS表")
    private Integer productId;

    @ApiModelProperty(value = "用户ID，外键关联USERS表")
    private Integer userId;

    @ApiModelProperty(value = "评分（1-5）")
    private Integer rating;

    @ApiModelProperty(value = "评价内容")
    private String comment;

    @ApiModelProperty(value = "评价内容-模糊匹配")
    private String commentLike;

    @ApiModelProperty(value = "评价时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "描述-模糊匹配")
    private String descriptionLike;

    @ApiModelProperty(value = "生效标志(TRUE-生效, FALSE-失效)")
    private Boolean validFlag;



}
