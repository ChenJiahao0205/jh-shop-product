package pers.jhshop.product.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 通用标签VO
 * @author ChenJiahao(五条)
 * @date 2025/01/08 22:54:56
 */
@Data
public class AllLabelIdAndNameAndSubVO {

    List<LabelIdAndNameAndSub> allCategoriesInfo;

    @Data
    public static class LabelIdAndNameAndSub {

        /**
         * 标签ID
         */
        private Long labelId;

        /**
         * 标签名称
         */
        private String labelName;

        /**
         * 子标签信息
         */
        private List<LabelIdAndNameAndSub> subList;
    }
}
