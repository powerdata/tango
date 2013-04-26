/*
   Copyright 2013 Incremental Systems Corporation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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
	
	final public Complex inv()
	{
		float den = _re*_re+_im*_im;
		return new Complex(_re/den, _im/-den);
	}

	final public Complex conjg()
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
	
	final public Complex sub(Complex v)
	{
		return new Complex(_re - v._re, _im - v._im);
	}
	
	final public Complex sub(float re2, float im2)
	{
		return new Complex(_re - re2, _im - im2);
	}
	
	final public Complex mult(float scalar)
	{
		return new Complex(_re*scalar, _im*scalar);
	}

	final public Complex mult(Complex v)
	{
		return mult(v._re, v._im);
	}
	
	final public Complex mult(float re2, float im2)
	{
		return new Complex(_re*re2-_im*im2, _im*re2+_re*im2);
	}

	final public Complex div(float scalar)
	{
		return new Complex(_re/scalar, _im/scalar);
	}

	final public Complex div(Complex divisor)
	{
		return div(divisor._re, divisor._im);
	}
	
	final public Complex div(float divre, float divim)
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

	@Override
	public boolean equals(Object obj)
	{
		Complex o = (Complex) obj;
		return (_re == o._re && _im == o._im);
	}
	
}
