package pers.jhshop.product.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import pers.jhshop.common.entity.BaseVo;

/**
 * <p>
 * 商品分类表VO
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CategoriesVO", description = "商品分类表列表展示VO")
public class CategoriesVO extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类唯一标识")
    private Long id;

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

}
