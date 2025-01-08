package pers.jhshop.product.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 所有标签VO
 * @author ChenJiahao(五条)
 * @date 2025/01/08 22:54:56
 */
@Data
public class AllCategoriesInfoVO {

    List<CategoryIdAndName> allCategoriesInfo;

    @Data
    public static class CategoryIdAndName {

        /**
         * 商品分类ID
         */
        private Long productCategoryId;

        /**
         * 商品分类名称
         */
        private String productCategoryName;

        /**
         * 子商品标签信息
         */
        private List<CategoryIdAndName> subCategoryIdAndNameList;
    }
}
