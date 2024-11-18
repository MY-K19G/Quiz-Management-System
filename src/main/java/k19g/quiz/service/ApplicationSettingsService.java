package k19g.quiz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import k19g.quiz.entity.ApplicationSettings;
import k19g.quiz.repository.ApplicationSettingsRepository;

/**
* <p>
* This service handles the saving and retrieving of application-wide settings,
* specifically.
* </p>
* 
* <p><strong>Author:</strong> K19G</p>
*/
@Service
public class ApplicationSettingsService {
	
    private static final Logger logger = LoggerFactory.getLogger(ApplicationSettingsService.class);
	
	private final ApplicationSettingsRepository  applicationSettingsRepository;
	
	@Autowired
	private ApplicationSettingsService( ApplicationSettingsRepository  applicationSettingsRepository) {
		this.applicationSettingsRepository = applicationSettingsRepository;
		}
	
    /**
     * Saves or updates the toggle register setting.
     * <p>
     * This method saves the provided boolean value for the toggle setting in the database.
     * If the setting row does not exist (checked by ID 1), it will create a new row with this ID.
     * If the row exists, it updates the existing row to the new value.
     * </p>
     *
     * @param value the boolean value to set for the toggle register.
     */
    public void setToggleRegister(boolean value) {
        
    	logger.info("Attempting to save the toggle register setting with value: {}", value);

    	 ApplicationSettings settings = applicationSettingsRepository.findById(1)
                 .orElse(new ApplicationSettings()); 
    	 
    	 System.err.println(settings);
         
    	 settings.setSettingValue(value);

         applicationSettingsRepository.save(settings);
         
         logger.info("Toggle register setting saved with value: {}", value);

    }
    
    /**
     * Retrieves the current value of the toggle register setting.
     * <p>
     * This method checks the database for the setting with ID 1 and returns its current value.
     * </p>
     *
     * @return the boolean value of the toggle register setting.
     */
    public boolean getToggleRegister() {
        
    	logger.info("Retrieving the current value of the toggle register setting.");

        boolean value = applicationSettingsRepository.findById(1)
            .map(ApplicationSettings::isSettingValue)
            .orElse(false);

        logger.info("Current toggle register setting retrieved: {}", value);

    	return value;
    }
}
