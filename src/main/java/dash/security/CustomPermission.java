package dash.security;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

/**
 * This class defines new permissions to be used by ACL/ACEs. The Manager and
 * Member permissions are currently turned off as they were specifically used in
 * the VMA app but can be turned on or modified for your buisness logic.
 * 
 * @author Tyler.swensen@gmail.com
 *
 */

public class CustomPermission extends BasePermission {

	private static final long serialVersionUID = -8963735762723530908L;

	public static final Permission DELETE_RESPONSES = new CustomPermission(1 << 7, 'r'); // 128

	protected CustomPermission(int mask) {
		super(mask);
	}

	protected CustomPermission(int mask, char code) {
		super(mask, code);
	}
}