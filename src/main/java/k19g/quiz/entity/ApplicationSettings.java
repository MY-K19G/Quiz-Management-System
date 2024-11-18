package k19g.quiz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * <h2>ApplicationSettings</h2>
 * This entity represents application-level settings that are stored in the database.
 * It contains key-value settings that can be updated and retrieved as needed.
 *
 * <p><strong>Author:</strong> K19G</p>
 */
@Entity
public class ApplicationSettings {

	@Id
	private Integer id = 1;
	
	private String settingKey="isRegisterEndpoint";
	
	private boolean settingValue=true;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String isSettingKey() {
		return settingKey;
	}
	
	public boolean isSettingValue() {
		return settingValue;
	}

	public void setSettingValue(boolean settingValue) {
		this.settingValue = settingValue;
	}

	@Override
	public String toString() {
		return "ApplicationSettings [id=" + id + ", settingKey=" + settingKey + ", settingValue=" + settingValue + "]";
	}

	
}
