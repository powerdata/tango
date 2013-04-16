package com.incsys.tango;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parse tango Fortran results file and create CSV files for each unit.
 * @author chris
 *
 */
public class ResultParser
{
	static private final Pattern _WS = Pattern.compile("\\s+");
	static final private int _VALPERLN = 12;
	static final private String _GENCNTID = "0NO. OF GENERATORS";
	File _outputdir;
	List<PrintWriter> _ow;
	
	public ResultParser(File outdir)
	{
		_outputdir = outdir;
	}

	public void parse(BufferedReader ir) throws IOException
	{
		int ngen = setupGen(ir);
		findDataStart(ir);
		String l = ir.readLine();
		while (l != null && !l.startsWith("1"))
		{
			if (l.contains("TERMINAL ADMITTANCE MATRIX"))
			{
				skipMatrix(ir, ngen);
				l = ir.readLine();
			}
			/* get the time */
			float time = Float.parseFloat(l.trim());
			for (int i = 0; i < ngen; ++i)
			{
				l = ir.readLine().trim();
				/* split around whitespace */
				String[] p = _WS.split(l);
				/* find the generator number and associated file */
				PrintWriter o = _ow.get(i);
				o.print(time);
				for (int j = 1; j < _VALPERLN; ++j)
				{
					o.print(',');
					o.print(p[j]);
				}
				o.println();
			}
			l = ir.readLine();
		}
		cleanup();
	}
	
	private void skipMatrix(BufferedReader ir, int ngen) throws IOException
	{
		/*
		 * there is one line per node, and one node per generator, so skip ngen
		 * lines
		 */
		for (int i = 0; i < ngen; ++i)
			ir.readLine();
		/* also skip the terminating 0 */
		ir.readLine();
	}

	private void findDataStart(BufferedReader ir) throws IOException
	{
		String l = ir.readLine();
		while (!l.contains("SECS   NO  ANGLE"))
			l = ir.readLine();
		/* skip 1 more line */
		ir.readLine();
	}

	private int setupGen(BufferedReader ir) throws IOException
	{
		String l = ir.readLine();
		while (!l.startsWith(_GENCNTID))
			l = ir.readLine();
		String gn = l.substring(_GENCNTID.length()).trim();
		int ngen = Integer.parseInt(gn);
		_ow = new ArrayList<>(ngen);
		for (int i = 0; i < ngen; ++i)
		{
			PrintWriter o =
				new PrintWriter(new BufferedWriter(new FileWriter(new File(
					_outputdir, String.valueOf(i + 1) + ".csv"))));
			o.print("Time,Rotor Angle,Rotor Speed,EQ' Volts, ED' Volts,");
			o.print("Term Volts,Elec Power Re,Elec Pwr Im,Fld Volts,");
			o.println("Mech Power, Satn Factor, Air Gap Volts,Elec MW,Elec MVAr");
			_ow.add(o);
		}
		return ngen;
	}

	protected void cleanup()
	{
		for(PrintWriter w : _ow)
			w.close();
		_ow.clear();
	}
	
	static public void main(String[] args) throws IOException
	{

		String userdir = System.getProperty("user.dir");
		String outdir = userdir;
		String tangoresults = null;

		int narg = args.length;
		for (int i = 0; i < narg;)
		{
			String a = args[i++];
			if (a.startsWith("-"))
			{
				int idx = (a.charAt(1) == '-') ? 2 : 1;
				switch (a.substring(idx))
				{
				case "outdir":
					outdir = args[i++];
					break;
				case "tangoresults":
					tangoresults = args[i++];
					break;
				case "h":
				case "help":
					showHelp();
					System.exit(0);
				default:
					System.out.println("parameter " + a + " not understood");
					showHelp();
					System.exit(1);
				}
			}
		}

		if (tangoresults == null)
		{
			System.out.println("Unspecified tangoresults parameter");
			showHelp();
			System.exit(1);
		}

		File out = new File(outdir);
		if (!out.exists()) out.mkdirs();

		BufferedReader ir = new BufferedReader(new FileReader(tangoresults));

		ResultParser rp = new ResultParser(out);
		rp.parse(ir);
		ir.close();
	}

	public static void showHelp()
	{
		System.out.println("usage: --tangoresults fortran_tango_results_file [ --outdir output_directory ] [ --help ]");
	}

}
