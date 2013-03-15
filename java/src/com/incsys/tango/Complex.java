package com.incsys.tango;

/**
 * Immutable complex variable
 * 
 * During testing, we noticed that use of the "final" modifier for the class and
 * fields cause a significant speed reduction during construction. "final" in
 * the getter methods seem to work ok
 * 
 * @author chris
 * 
 */
public class Complex
{
	private float _re;
	private float _im;
	
	public static final Complex Zero = new Complex(0,0);

	public Complex(float re, float im)
	{
		this._re = re;
		this._im = im;
	}

	final public float re() {return _re;}
	final public float im() {return _im;}
	
	public Complex invert()
	{
		float den = _re*_re+_im*_im;
		return new Complex(_re/den, _im/-den);
	}

	final public Complex conjugate()
	{
		return new Complex(_re, _im*-1);
	}
	
	final public float abs()
	{
		return (float) Math.sqrt(_re*_re+_im*_im);
	}

	final public Complex add(Complex v)
	{
		return new Complex(_re+v._re, _im+v._im);
	}
	
	final public Complex add(float re2, float im2)
	{
		return new Complex(_re+re2, _im+im2);
	}
	
	final public Complex subtract(Complex v)
	{
		return new Complex(_re - v._re, _im - v._im);
	}
	
	final public Complex subtract(float re2, float im2)
	{
		return new Complex(_re - re2, _im - im2);
	}
	
	final public Complex multiply(float scalar)
	{
		return new Complex(_re*scalar, _im*scalar);
	}

	final public Complex multiply(Complex v)
	{
		return multiply(v._re, v._im);
	}
	
	final public Complex multiply(float re2, float im2)
	{
		return new Complex(_re*re2-_im*im2, _im*re2+_re*im2);
	}

	final public Complex divide(float scalar)
	{
		return new Complex(_re/scalar, _im/scalar);
	}

	final public Complex divide(Complex divisor)
	{
		return divide(divisor._re, divisor._im);
	}
	
	final public Complex divide(float divre, float divim)
	{
		float den = divre * divre + divim * divim;
		return new Complex((_re * divre + _im * divim) / den,
			(_im * divre - _re * divim) / den);
	}

	@Override
	final public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(_re);
		sb.append(',');
		sb.append(_im);
		sb.append(')');
		return sb.toString();
	}
	
}
