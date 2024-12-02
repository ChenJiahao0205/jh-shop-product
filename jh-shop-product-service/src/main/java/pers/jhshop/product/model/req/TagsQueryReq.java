package pers.jhshop.product.model.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.entity.Tags;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品标签表查询Req
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TagsQueryReq", description = "商品标签表查询Req")
public class TagsQueryReq extends Page<Tags> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签唯一标识")
    private Long id;

    @ApiModelProperty(value = "商品ID，外键关联PRODUCTS表")
    private Integer productId;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "标签名称-模糊匹配")
    private String tagNameLike;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "描述-模糊匹配")
    private String descriptionLike;

    @ApiModelProperty(value = "生效标志(TRUE-生效, FALSE-失效)")
    private Boolean validFlag;



}
