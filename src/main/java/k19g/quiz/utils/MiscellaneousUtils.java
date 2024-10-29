package k19g.quiz.utils;

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
	 * For example:
	 * - If the input time is 0 seconds, the result will be "0s".
	 * - If there are no hours, minutes, or seconds to display, the corresponding parts are omitted.
	 * 
	 * @param seconds The total time in seconds to convert.
	 * @return A formatted time string in the form of "Xh Ym Zs", where X, Y, and Z are the number of hours, minutes,
	 *         and seconds, respectively. If hours, minutes, or seconds are zero, they will be omitted from the result.
	 *         If all values are zero, the result will be "0s".
	 */
	public static String convertSecondsToTime(long seconds) {
        logger.info("Converting {} seconds to time format.", seconds); // Log the input value

	    long hours = seconds / 3600;                    // Calculate the number of hours
	    long minutes = (seconds % 3600) / 60;           // Calculate the number of minutes
	    long remainingSeconds = seconds % 60;           // Calculate the remaining seconds

	    StringBuilder timeBuilder = new StringBuilder(); // StringBuilder to construct the time string

	    // Only add hours if they are non-zero
	    if (hours > 0) {
	        timeBuilder.append(hours).append("h ");     // Append hours to the time string
	    }

	    // Only add minutes if they are non-zero
	    if (minutes > 0) {
	        timeBuilder.append(minutes).append("m ");   // Append minutes to the time string
	    }

	    // Only add seconds if they are non-zero
	    if (remainingSeconds > 0) {
	        timeBuilder.append(remainingSeconds).append("s"); // Append seconds to the time string
	    }

	    // If no values were added (e.g., 0 seconds input), return "0s"
	    if (timeBuilder.length() == 0) {
	        return "0s";
	    }
	    
	    // Return the formatted string without any trailing spaces
	    String formattedTime = timeBuilder.toString().trim(); // Return the formatted string without any trailing spaces
        logger.info("Converted time: {}", formattedTime); // Log the output value
        
        return formattedTime;

	}

}
