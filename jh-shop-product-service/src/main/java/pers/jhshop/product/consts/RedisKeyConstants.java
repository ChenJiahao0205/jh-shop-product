package pers.jhshop.product.consts;

/**
 * Redis key常量类
 * @author ChenJiahao(五条)
 * @date 2025/01/09 22:29:36
 */
public interface RedisKeyConstants {

    String PRODUCT_REDIS_KEY_PREFIX = "product-";

    String ALL_PRODUCT_CATEGORY_REDIS_KEY = PRODUCT_REDIS_KEY_PREFIX + "allProductCategoryRedisKey";

}
