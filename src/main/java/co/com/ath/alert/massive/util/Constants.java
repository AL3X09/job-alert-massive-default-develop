package co.com.ath.alert.massive.util;

import org.hibernate.boot.cfgxml.internal.ConfigLoader;

public class Constants {

	public static final String HEALTH_STATUS_OK = "OK";
	public static final String JOB_MASSIVE_ALERT_DEFAULT = "JOB_MASSIVE_ALERT_DEFAULT";
	public static final String JOB_MASSIVE_ALERT_DEFAULT_OPERATION_SAVE = "processFileSaveAlertMassive";
	public static final String JOB_MASSIVE_ALERT_DEFAULT_OPERATION_INACTIVATE = "processFileInactivateAlertMassive";
	public static final String JOB_MASSIVE_ALERT_DEFAULT_OPERATION_HEALTH = "processGetHealthAlertMassive";

	/* creación masivas */
	public static final String NAME_CATALOG_TYPE = "ALERT_MASSIVE_DEFAULT";
	public static final String KEY_CATALOG_PATH_DAILY = "Daily_Path_file";

	/* inactivación */
	public static final String JOB_MASSIVE_INACTIVATE_ALERT_DEFAULT = "JOB_MASSIVE_INACTIVATE_ALERT_DEFAULT";
	public static final String KEY_CATALOG_INACTIVATE_PATH_DAILY = "Daily_Inactivate_Path_file";

	public static final String GENERIC_BANK = "00000";
	public static final String BANK_BPOP = "00010029";
	public static final String DATE_FORMAT_FILE = "YYYYMMDD";

	public static final String ACTIVECODE = "0";
	public static final String INACTIVECODE = "2";

	public static final String TARGET_TYPE_CELL = "CELL";
	public static final String TARGET_TYPE_EMAIL = "EMAIL";
	public static final String NOT_IS_BM = "N";

	public static final String CODE_SUCCESS = "0";
	public static final String SUCCESS = "Info";
	public static final String ERROR = "Error";
	public static final String MSGSUCCESS = "Transacción Exitosa";
	public static final String MSGERROR = "Fallas al procesar las alertas del archivo";
}
