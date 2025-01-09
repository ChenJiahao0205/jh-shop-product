package pers.jhshop.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品分类级别枚举
 * @author ChenJiahao(五条)
 * @date 2025/01/09 22:20:08
 */
@Getter
@AllArgsConstructor
public enum CategoryParentIdLevelEnum {

    /**
     * 商品分类父类
     */
    ROOT_CATEGORY(0L);

    private final Long value;
}
