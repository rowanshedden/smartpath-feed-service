package aero.sitalab.idm.feed.exception;

import org.springframework.stereotype.Component;

/**
 * ServiceException
 */
@Component
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -3026381549383437315L;

	/**
	 * Instantiates a new service exception.
	 */
	public ServiceException() {
		super();
	}

	/**
	 * Instantiates a new service exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new service exception.
	 *
	 * @param message
	 *            the message
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * Intercept message, set and throw exception
	 * 
	 * @param message
	 * @return
	 */
	public ServiceException setAndThrowException(String message) {
		if (message == null)
			message = "NullPointerException";
		throw new ServiceException(message);
	}
}
