package k19g.quiz.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>Miscellaneous Utilities</h1>
 * <p>The {@code MiscellaneousUtils} class provides utility methods for handling miscellaneous tasks.</p>
 * 
 * <p>This class is designed for utility purposes and should be used in a static context, without the
 * need to create instances.</p>
 */
public class MiscellaneousUtils {
		
    private static final Logger logger = LoggerFactory.getLogger(MiscellaneousUtils.class);

	/**
	 * Converts a given time in seconds to a formatted string displaying hours, minutes, and seconds.
	 * 
	 * <p>The method only includes hours, minutes, or seconds in the result if they are non-zero.
	 * <p>For example: If the input time is 0 seconds, the result will be "0s".
	 * If there are no hours, minutes, or seconds to display, the corresponding parts are omitted.</p>
	 * 
	 * @param seconds The total time in seconds to convert.
	 * @return A formatted time string in the form of "Xh Ym Zs", where X, Y, and Z are the number of hours, minutes,
	 *         and seconds, respectively. If hours, minutes, or seconds are zero, they will be omitted from the result.
	 *         If all values are zero, the result will be "0s".
	 */
	public static String convertSecondsToTime(long seconds) {
        logger.info("Converting {} seconds to time format.", seconds); 

	    long hours = seconds / 3600;                    
	    long minutes = (seconds % 3600) / 60;           
	    long remainingSeconds = seconds % 60;           

	    StringBuilder timeBuilder = new StringBuilder(); 

	    if (hours > 0) {
	        timeBuilder.append(hours).append("h ");    
	    }

	    if (minutes > 0) {
	        timeBuilder.append(minutes).append("m ");   
	    }

	    if (remainingSeconds > 0) {
	        timeBuilder.append(remainingSeconds).append("s"); 
	    }

	    if (timeBuilder.length() == 0) {
	        return "0s";
	    }
	    
	    String formattedTime = timeBuilder.toString().trim(); 
        logger.info("Converted time: {}", formattedTime); 
        
        return formattedTime;
	}
	
	/**
     * Checks if a list is empty or null and throws a custom exception if true.
     * 
     * @param list the list to check
     * @param exception the exception to throw if the list is empty or null
     * @param <T> the type of the list
     * @throws RuntimeException if the list is empty or null
     */
    public static <T> void checkIfListIsEmpty(List<T> list, RuntimeException exception) {
        if (list == null || list.isEmpty()) {
            throw exception;
        }
    }
    
    /**
     * Checks if a given object/Optional/Collection is empty or null and throws a custom exception if true.
     * 
     * @param ref for check
     * @param exception the exception to throw if the ref is empty or null
     * @param <T> the type of the list
     * @throws RuntimeException if the list is empty or null
     */
    public static <T> void checkIfEmpty(Object ref, RuntimeException exception) {
        if (ref == null) {
            throw exception;
        }
        
        if (ref instanceof Optional) {
            Optional<?> optionalRef = (Optional<?>) ref; 
            if (!optionalRef.isPresent()) {
                throw exception;
            }
        }
        
        if (ref instanceof Collection) {
            Collection<?> collectionRef = (Collection<?>) ref; 
            if (collectionRef.isEmpty()) {
                throw exception;
            }
        }
    }

    /**
     * <p>Finds the indexes in the <code>sourceList</code> where elements match those in the <code>targetList</code>.</p>
     * 
     * @param sourceList the list in which you are searching for matching elements from the <code>targetList</code>.
     * @param targetList the list that contains the elements you're trying to find in the <code>sourceList</code>.
     * @return a list of integer indexes from the <code>targetList</code> where matches were found.
     */
    public static  List<Integer> findMatchingIndexes(List<String> sourceList, List<String> targetList) {
        List<Integer> matchingIndices = new ArrayList<>();
        
        for (int i = 0; i < sourceList.size(); i++) {
            String str1 = sourceList.get(i);
            
            for(int j=0;j<targetList.size();j++) {
            	
            	 if (str1.equals(targetList.get(j))) {
            		  matchingIndices.add(j);  
	                    break;  
	                }
            }
        }
        logger.info("Completed findMatchingIndexes. Matching indexes: {}", matchingIndices);
        return matchingIndices;
    }

}
