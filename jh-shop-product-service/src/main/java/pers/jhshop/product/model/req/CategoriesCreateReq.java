package pers.jhshop.product.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品分类表新增Req
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CategoriesCreateReq", description = "商品分类表新增Req")
public class CategoriesCreateReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父级分类ID，0表示根分类")
    private Integer parentId;

    @ApiModelProperty(value = "分类名称(如“手机”，“家电”，“衣物”等)")
    private String name;

    @ApiModelProperty(value = "分类描述")
    private String productCategoriesDescription;

    @ApiModelProperty(value = "分类创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "分类最后更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "生效标志(TRUE-生效, FALSE-失效)")
    private Boolean validFlag;
}
