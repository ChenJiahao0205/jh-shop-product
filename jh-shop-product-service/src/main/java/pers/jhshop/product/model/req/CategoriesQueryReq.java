package pers.jhshop.product.model.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.entity.Categories;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品分类表查询Req
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CategoriesQueryReq", description = "商品分类表查询Req")
public class CategoriesQueryReq extends Page<Categories> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类唯一标识")
    private Long id;

    @ApiModelProperty(value = "父级分类ID，0表示根分类")
    private Integer parentId;

    @ApiModelProperty(value = "分类名称(如“手机”，“家电”，“衣物”等)")
    private String name;

    @ApiModelProperty(value = "分类名称(如“手机”，“家电”，“衣物”等)-模糊匹配")
    private String nameLike;

    @ApiModelProperty(value = "分类描述")
    private String productCategoriesDescription;

    @ApiModelProperty(value = "分类描述-模糊匹配")
    private String productCategoriesDescriptionLike;

    @ApiModelProperty(value = "分类创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "分类最后更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "描述-模糊匹配")
    private String descriptionLike;

    @ApiModelProperty(value = "生效标志(TRUE-生效, FALSE-失效)")
    private Boolean validFlag;



}