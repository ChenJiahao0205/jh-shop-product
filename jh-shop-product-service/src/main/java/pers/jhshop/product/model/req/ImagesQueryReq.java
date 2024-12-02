package pers.jhshop.product.model.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.entity.Images;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品图片表查询Req
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ImagesQueryReq", description = "商品图片表查询Req")
public class ImagesQueryReq extends Page<Images> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片唯一标识")
    private Long id;

    @ApiModelProperty(value = "商品ID，外键关联PRODUCTS表")
    private Integer productId;

    @ApiModelProperty(value = "图片URL")
    private String imageUrl;

    @ApiModelProperty(value = "图片URL-模糊匹配")
    private String imageUrlLike;

    @ApiModelProperty(value = "是否是主图（1=是，0=否）")
    private Boolean isMain;

    @ApiModelProperty(value = "图片上传时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "描述-模糊匹配")
    private String descriptionLike;

    @ApiModelProperty(value = "生效标志(TRUE-生效, FALSE-失效)")
    private Boolean validFlag;



}
