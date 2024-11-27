package k19g.quiz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.hibernate.cache.CacheException;
import org.hibernate.collection.spi.PersistentBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import k19g.quiz.entity.Quiz;

/**
* <p>
* The {@code QuizCacheManager} class provides methods to manage quiz-related caches in the application.
* It supports creating, updating, and deleting cached data for various quiz attributes and entities.
* </p>
* 
* <h2>Features:</h2>
* <ul>
*     <li>Clears and updates specific quiz-related caches based on operations.</li>
*     <li>Handles caches containing lists of various types including {@link Quiz}, {@link String}, and {@link Enum}.</li>
*     <li>Integrates with Caffeine {@link CacheManager} for Spring-managed caching.</li>
* </ul>
* 
* @author K19G
*/
@Component
public class QuizCacheManager {

	private static final Logger logger = LoggerFactory.getLogger(QuizCacheManager.class);
	
	@Autowired
	private CacheManager cacheManager;

	/**
     * Clears or updates a cache entry for the specified cache name and key.
     *
     * @param cacheName      the name of the cache
     * @param cacheId        the cache key
     * @param oldCacheElement the old cache element (before update or delete)
     * @param latestCacheElement the new cache element (for update or create)
     * @param mode           the operation mode ("create", "update", or "delete")
     * @param <T>            the type of the cache key
     * @throws CacheException if the cache is not found or unsupported cache type is encountered
     */
    private  <T> void clearCache(String cacheName, T cacheId, Object oldCachecEle, Object latestCacheEle, String mode) {
        logger.info("Clearing cache: {}, Cache ID: {}, Mode: {}", cacheName, cacheId, mode);
        
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            logger.error("No cache found: {}", cacheName);
            throw new CacheException("No cache found: " + cacheName);
        }

        ValueWrapper cachedWrapper = cache.get(cacheId);
        if (cachedWrapper == null) {
            logger.info("No value found in cache for ID: {}, nothing to clear.", cacheId);
            return;
        }

        Object cachedValue = cachedWrapper.get();
        logger.debug("Cached value retrieved for ID {}: {}", cacheId, cachedValue);

        if (cachedValue instanceof List<?>) {
            logger.info("Handling list cache for cache ID: {}", cacheId);
            handleListCache((List<?>) cachedValue, oldCachecEle, latestCacheEle, mode);
            cache.put(cacheId, cachedValue); 
            logger.info("Cache updated for cache ID: {}", cacheId);
        } else {
            logger.error("Unsupported cache value type: {}", cachedValue.getClass().getName());
            throw new UnsupportedOperationException("Unsupported cache value type: " + cachedValue.getClass().getName());
        }
    }

    /**
     * Handles caching for list-based values, performing operations like create, update, or delete.
     *
     * @param cachedList      the list currently cached
     * @param oldCachedElement the old element to be replaced or removed
     * @param latestCacheElement the new element to be added or updated
     * @param mode            the operation mode ("create", "update", or "delete")
     * @param <T>             the type of the elements in the list
     * 
     * @throws CacheException if the cache is not found
     * @throws UnsupportedOperationException if the cache value type is not supported
     */
    @SuppressWarnings( "unchecked" )
	private <T> void handleListCache(List<?> cachedList, Object OldCachedEle, Object latestCacheEle, String mode) {
        logger.debug("Handling list cache with mode: {}, Old Cached Element: {}, Latest Cached Element: {}", mode, OldCachedEle, latestCacheEle);

        if (cachedList.isEmpty()) {
            logger.info("Cached list is empty, no operations performed.");
            return;
        }

        Object firstElement = cachedList.get(0);
        logger.debug("First element type in cached list: {}", firstElement.getClass().getName());

        if (firstElement instanceof Quiz) {
            modifyList((List<Quiz>) cachedList, (Quiz) OldCachedEle, (Quiz) latestCacheEle, mode);
        } else if (firstElement instanceof String && !(OldCachedEle instanceof PersistentBag<?>)) {
            modifyList((List<String>) cachedList, (String) OldCachedEle, (String) latestCacheEle, mode);
        } else if (OldCachedEle instanceof PersistentBag<?>) {
            modifyList((List<String>) cachedList, (List<String>) OldCachedEle, (List<String>) latestCacheEle, mode);
        } else if (firstElement instanceof Enum<?>) {
            handleEnumCache((List<Enum<?>>) cachedList, (String) OldCachedEle, (String) latestCacheEle, mode);
        } else {
            logger.error("Unsupported list type: {}", firstElement.getClass().getName());
            throw new UnsupportedOperationException("Unsupported list type: " + firstElement.getClass().getName());
        }
    }

    /**
     * Handles caching updates for a list of enumerations by modifying the cache
     * based on the provided mode (create, update, or delete).
     *
     * <p>
     * This method determines the actual enumeration instances based on the provided
     * strings and delegates the modification logic to the {@code modifyList} method.
     * </p>
     *
     * @param cachedList    the current list of cached enumeration values
     * @param OldCachedEle  the string representation of the old cached enumeration value, or {@code null} if not applicable
     * @param latestCacheEle the string representation of the new enumeration value to be added or updated
     * @param mode          the operation mode indicating the type of cache update
     *                      (e.g., "create", "update", or "delete")
     * @throws IllegalArgumentException if the provided {@code latestCacheEle} does not correspond to a valid enumeration value
     */
    @SuppressWarnings("unchecked")
	private void handleEnumCache(List<Enum<?>> cachedList, String OldCachedEle, String latestCacheEle, String mode) {
        logger.debug("Handling enum cache with mode: {}, Old Cached Element: {}, Latest Cached Element: {}", mode, OldCachedEle, latestCacheEle);

        Enum<?> realEnum = Enum.valueOf((Class<Enum>) cachedList.get(0).getClass(), latestCacheEle);
        Enum<?> cachedEnum = OldCachedEle != null ? Enum.valueOf((Class<Enum>) cachedList.get(0).getClass(), OldCachedEle) : null;

        modifyList((List<Enum<?>>) cachedList, cachedEnum, realEnum, mode);
    }

    /**
     * Modifies a list by adding, updating, or removing a single element
     * based on the specified operation mode.
     *
     * <p>
     * This method performs the following actions based on the mode:
     * <ul>
     *     <li>"create": Adds the {@code latestCacheEle} to the list.</li>
     *     <li>"update": Replaces the {@code OldCachedEle} with {@code latestCacheEle}.</li>
     *     <li>"delete": Removes the {@code OldCachedEle} from the list.</li>
     * </ul>
     * </p>
     *
     * @param <T>            the type of elements in the list
     * @param cachedList     the list to be modified
     * @param OldCachedEle   the old element to be removed or replaced (can be {@code null} for "create")
     * @param latestCacheEle the new element to be added or updated
     * @param mode           the operation mode indicating the type of modification ("create", "update", or "delete")
     */
    private <T> void modifyList(List<T> cachedList, T OldCachedEle, T latestCacheEle, String mode) {
        logger.debug("Modifying list. Mode: {}, Old Cached Element: {}, Latest Cached Element: {}", mode, OldCachedEle, latestCacheEle);
        logger.debug("Before modification: {}", cachedList);

        switch (mode) {
            case "delete":
            	cachedList.removeIf(item -> item.equals(OldCachedEle));
                break;
            case "update":
                int index = cachedList.indexOf(OldCachedEle);
                if (index >= 0) {
                	cachedList.set(index, latestCacheEle);
                }
                break;
            case "create":
            	cachedList.add(latestCacheEle);
                break;
        }

        logger.debug("After modification: {}", cachedList);
    }

   /**
    * Modifies a list by adding, updating, or removing multiple elements
    * based on the specified operation mode.
    *
    * <p>
    * This method performs the following actions based on the mode:
    * <ul>
    *     <li>"create": Adds all elements from {@code latestCacheEle} to the list.</li>
    *     <li>"update": Removes all elements from {@code OldCachedEle} and adds all elements from {@code latestCacheEle}.</li>
    *     <li>"delete": Removes all elements from {@code OldCachedEle}.</li>
    * </ul>
    * </p>
    *
    * @param <T>            the type of elements in the list
    * @param list           the list to be modified
    * @param OldCachedEle   the list of old elements to be removed (can be {@code null} for "create")
    * @param latestCacheEle the list of new elements to be added or updated
    * @param mode           the operation mode indicating the type of modification ("create", "update", or "delete")
    */
    private <T> void modifyList(List<T> cachedList, List<T> OldCachedEle, List<T> latestCacheEle, String mode) {
        logger.debug("Modifying list (bulk operation). Mode: {}, Old Cached Element: {}, Latest Cached Elements: {}", mode, OldCachedEle, latestCacheEle);
        logger.debug("Before modification: {}", cachedList);

        switch (mode) {
            case "delete":
            	cachedList.removeAll(OldCachedEle);
                break;
            case "update":
            	cachedList.removeAll(OldCachedEle);
            	cachedList.addAll(latestCacheEle);
                break;
            case "create":
            	cachedList.addAll(latestCacheEle);
                break;
        }

        logger.debug("After modification: {}", cachedList);
    }

    /**
     * Updates or modifies cached entries for quizzes.
     * 
     * @param targetQuiz the {@link Quiz} object to be updated in the cache
     * @param mode       the operation mode ("create", "update", or "delete")
     * 
     * @throws CacheException if any cache-related error occurs
     */
    public void updateCache(Quiz targetQuiz, String mode) {
        logger.info("Updating cache for Quiz ID: {}, Mode: {}", targetQuiz.getId(), mode);

        if ("create".equals(mode)) {
            performCacheUpdates(targetQuiz, null, mode);
        } else {
            Quiz oldQuiz = findOldQuiz(targetQuiz.getId());
            if (oldQuiz != null) {
                logger.info("Old quiz found for ID: {}", targetQuiz.getId());
                performCacheUpdates(targetQuiz, oldQuiz, mode);
            } else {
                logger.warn("No old quiz found for ID: {}", targetQuiz.getId());
            }
        }
    }

   /**
    * Performs cache updates for a given quiz, ensuring relevant caches are updated or cleared
    * based on the provided operation mode (create, update, or delete).
    *
    * <p>
    * This method updates the following caches:
    * <ul>
    *     <li>{@code quizCategoriesCache}: Updates categories for quizzes.</li>
    *     <li>{@code quizTypesCache}: Updates quiz types.</li>
    *     <li>{@code quizTitlesCache}: Updates quiz question titles.</li>
    *     <li>{@code distinctLevelsCache}: Updates distinct quiz levels.</li>
    *     <li>{@code answersCache}: Updates answers for a specific quiz.</li>
    *     <li>{@code getAllQuizzesCache}: Updates the cache containing all quizzes.</li>
    * </ul>
    * </p>
    *
    * @param latestQuiz the latest quiz object containing updated values
    * @param oldQuiz    the old quiz object containing previous values (can be {@code null} for create operations)
    * @param mode       the operation mode indicating the type of update (e.g., "create", "update", or "delete")
    * @throws IllegalArgumentException if {@code latestQuiz} or {@code mode} is {@code null}
    */
    private void performCacheUpdates(Quiz latestQuiz, Quiz oldQuiz, String mode) {
        logger.info("Performing cache updates for Quiz ID: {}, Mode: {}", latestQuiz.getId(), mode);

        clearCache("quizCategoriesCache", "getAllCategories", oldQuiz != null ? oldQuiz.getCategory() : null, latestQuiz.getCategory(), mode);
        clearCache("quizTypesCache", "getAllTypes", oldQuiz != null ? oldQuiz.getType() : null, latestQuiz.getType(), mode);
        clearCache("quizTitlesCache", "getAllQuizQuestionTitle", oldQuiz != null ? oldQuiz.getQuestionTitle() : null, latestQuiz.getQuestionTitle(), mode);
        clearCache("distinctLevelsCache", "getAllDistinctLevels", oldQuiz != null ? oldQuiz.getLevel().toString() : null, latestQuiz.getLevel().toString(), mode);
        clearCache("answersCache", latestQuiz.getId(), oldQuiz != null ? oldQuiz.getAnswers() : null, latestQuiz.getAnswers(), mode);
        clearCache("getAllQuizzesCache", "getAllQuizzes", oldQuiz, latestQuiz, mode);
    }

    /**
     * Finds and retrieves the old {@link Quiz} entity from the cache based on its ID.
     *
     * @param quizId the ID of the quiz
     * 
     * @return the old {@link Quiz} object if found, or {@code null} if not found
     */
    private Quiz findOldQuiz(int quizId) {
        logger.info("Finding old quiz for ID: {}", quizId);

        Cache cache = cacheManager.getCache("getAllQuizzesCache");
        if (cache == null) {
            logger.warn("Cache not found: getAllQuizzesCache");
            return null;
        }

        ValueWrapper quizCacheWrapper = cache.get("getAllQuizzes");
        if (quizCacheWrapper != null) {
            List<Quiz> quizList = (List<Quiz>) quizCacheWrapper.get();
            logger.debug("Quiz list retrieved from cache: {}", quizList);
            return quizList.stream().filter(q -> q.getId() == quizId).findFirst().orElse(null);
        }

        logger.warn("No quiz list found in cache for ID: {}", quizId);
        return null;
    }
}

