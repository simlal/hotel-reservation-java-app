package com.ift287lalonde;

// import java.io.Serial;

/**
 * L'exception BiblioException est levée lorsqu'une transaction est inadéquate.
 * Par exemple -- livre inexistant
 */
public final class IFT287Exception extends Exception
{
	// @Serial
	// private static final long serialVersionUID = 1L;

	public IFT287Exception(String message)
	{
		super(message);
	}
}