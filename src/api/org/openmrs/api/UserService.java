package org.openmrs.api;

import java.util.List;

import org.openmrs.Group;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOContext;
import org.openmrs.api.db.UserDAO;
import org.openmrs.util.OpenmrsConstants;

/**
 * User-related services
 * 
 * @author Burke Mamlin
 * @version 1.0
 */
public class UserService {
	
	private Context context;
	private DAOContext daoContext;
	
	public UserService(Context c, DAOContext d) {
		this.context = c;
		this.daoContext = d;
	}
	
	private UserDAO getUserDAO() {
		return daoContext.getUserDAO();
	}
	
	/**
	 * Create a new user
	 * @param user
	 * @param password
	 * @throws APIException
	 */
	public void createUser(User user, String password) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		getUserDAO().createUser(user, password);
	}

	/**
	 * Get user by internal user identifier
	 * @param userId internal identifier
	 * @return requested user
	 * @throws APIException
	 */
	public User getUser(Integer userId) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		return getUserDAO().getUser(userId);
	}
	
	/**
	 * Get user by username (user's login identifier)
	 * @param username user's identifier used for authentication
	 * @return requested user
	 * @throws APIException
	 */
	public User getUserByUsername(String username) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		return getUserDAO().getUserByUsername(username);
	}

	/**
	 * true/false if username is already in db
	 * @param User to compare
	 * @return boolean
	 * @throws APIException
	 */
	public boolean isDuplicateUsername(User user) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		return getUserDAO().isDuplicateUsername(user);
	}
	
	/**
	 * Get users by role granted
	 * @param Role role that the Users must have to be returned 
	 * @return users with requested role
	 * @throws APIException
	 */
	public List<User> getUsersByRole(Role role) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		return getUserDAO().getUsersByRole(role);
	}
	
	/**
	 * Save changes to user
	 * @param user
	 * @throws APIException
	 */
	public void updateUser(User user) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		getUserDAO().updateUser(user);
	}
	
	/**
	 * Grant roles for user
	 * @param user
	 * @param role
	 * @throws APIException
	 */
	public void grantUserRole(User user, Role role) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		getUserDAO().grantUserRole(user, role);
	}
	
	/**
	 * Revoke roles from user
	 * @param user
	 * @param role
	 * @throws APIException
	 */
	public void revokeUserRole(User user, Role role) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		getUserDAO().revokeUserRole(user, role);
	}

	/** 
	 * Mark user as voided (effectively deleting user without removing
	 * their data &mdash; since anything the user touched in the database
	 * will still have their internal identifier and point to the voided
	 * user for historical tracking purposes.
	 * 
	 * @param user
	 * @param reason
	 * @throws APIException
	 */
	public void voidUser(User user, String reason) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		getUserDAO().voidUser(user, reason);
	}
	
	/**
	 * Clear voided flag for user (equivalent to an "undelete" or
	 * Lazarus Effect for user)
	 * 
	 * @param user
	 * @throws APIException
	 */
	public void unvoidUser(User user) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		getUserDAO().unvoidUser(user);
	}
	
	/**
	 * Delete user from database. This is included for troubleshooting and
	 * low-level system administration. Ideally, this method should <b>never</b>
	 * be called &mdash; <code>Users</code> should be <em>voided</em> and
	 * not <em>deleted</em> altogether (since many foreign key constraints
	 * depend on users, deleting a user would require deleting all traces, and
	 * any historical trail would be lost).
	 * 
	 * This method only clears user roles and attempts to delete the user
	 * record. If the user has been included in any other parts of the database
	 * (through a foreign key), the attempt to delete the user will violate
	 * foreign key constraints and fail.
	 * 
	 * @param user
	 * @throws APIException
	 * @see #voidUser(User, String)
	 */
	public void deleteUser(User user) throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		getUserDAO().deleteUser(user);
	}
	
	/**
	 * Returns all privileges currently possible for any User
	 * @return Global list of privileges
	 * @throws APIException
	 */
	public List<Privilege> getPrivileges() throws APIException {
		return getUserDAO().getPrivileges();
	}
	
	/**
	 * Returns all roles currently possible for any User
	 * @return Global list of roles
	 * @throws APIException
	 */
	public List<Role> getRoles() throws APIException {
		return getUserDAO().getRoles();
	}

	/**
	 * Returns all users in the system
	 * @return Global list of users
	 * @throws APIException
	 */
	public List<User> getUsers() throws APIException {
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		return getUserDAO().getUsers();
	}

	/**
	 * Returns role object with given string role
	 * @return Role
	 * @throws APIException
	 */
	public Role getRole(String r) throws APIException {
		return getUserDAO().getRole(r);
	}

	/**
	 * Returns Privilege in the system with given String privilege
	 * @return Privilege
	 * @throws APIException
	 */
	public Privilege getPrivilege(String p) throws APIException {
		return getUserDAO().getPrivilege(p);
	}
	
	/**
	 * Returns all groups currently possible for any User
	 * @return Global list of groups
	 * @throws APIException
	 */
	public List<Group> getGroups() throws APIException {
		return getUserDAO().getGroups();
	}
	
	/**
	 * Returns group object with given string group
	 * @return Group
	 * @throws APIException
	 */
	public Group getGroup(String r) throws APIException {
		return getUserDAO().getGroup(r);
	}
	
	/**
	 * Changes the current user's password
	 * @param pw
	 * @param pw2
	 * @throws APIException
	 */
	public void changePassword(String pw, String pw2) throws APIException {
		// TODO or check if its the current user
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		getUserDAO().changePassword(pw, pw2);
	}
	
	public void changeQuestionAnswer(String pw, String q, String a) {
		// TODO or check if its the current user
		if (!context.hasPrivilege(OpenmrsConstants.PRIV_MANAGE_USERS))
			throw new APIAuthenticationException("Privilege required: " + OpenmrsConstants.PRIV_MANAGE_USERS);
		getUserDAO().changeQuestionAnswer(pw, q, a);
	}
	
	public List<User> findUsers(String name, List<String> roles, boolean includeVoided) {
		name = name.replace(", ", " ");
		return getUserDAO().findUsers(name, roles, includeVoided);
	}
}
