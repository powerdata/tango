package com.powerdata.pse.csvmemdb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CsvMemoryStore
{
	interface ModelDataInfo
	{
		public String getFileName();
		public <T extends PsrObject> Class<T> getClassObject();
	}
	enum ModelData implements ModelDataInfo
	{
		Organization(Organization.class),
		ControlArea(ControlArea.class),
		Substation(Substation.class), 
		Node(Node.class),
		Load(Load.class),
		GeneratingUnit(GeneratingUnit.class),
		SynchronousMachine(SynchronousMachine.class),
		Line(Line.class),
		Transformer(Transformer.class),
		TransformerWinding(TransformerWinding.class),
		RatioTapChanger(RatioTapChanger.class),
		Switch(Switch.class),
		SwitchType(SwitchType.class),
		PrimeMover(PrimeMover.class),
		ReactiveCapabilityCurve(ReactiveCapabilityCurve.class),
		SeriesReactor(SeriesDevice.class),
		SeriesCapacitor(SeriesDevice.class),
		ShuntReactor(SwitchedShunt.class),
		ShuntCapacitor(SwitchedShunt.class),
		SVC(SVC.class),
		PhaseTapChanger(PhaseTapChanger.class),
		Exciter(Exciter.class);

		String _filename;
		Class<? extends PsrObject> _class;
		
		ModelData(Class<? extends PsrObject> clobj)
		{
			_class = clobj;
			_filename = toString() + ".csv";
		}
		
		@Override
		public String getFileName() {return _filename;}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends PsrObject> Class<T> getClassObject()
		{
			return (Class<T>) _class;
		}
		
	}

	enum CaseData implements ModelDataInfo
	{
		ControlArea(BasecaseControlArea.class),
		Load(BasecaseLoad.class),
		GeneratingUnit(BasecaseGeneratingUnit.class),
		SynchronousMachine(BasecaseSynchronousMachine.class),
		Switch(BasecaseSwitch.class),
		Node(BasecaseNode.class);
		
		String _filename;
		Class<? extends PsrObject> _class;

		CaseData(Class<? extends PsrObject> clobj)
		{
			_class = clobj;
			StringBuilder sb = new StringBuilder("PsmCase");
			sb.append(toString());
			sb.append(".csv");
			_filename = sb.toString();
		}
		
		@Override
		public String getFileName() {return _filename;}
		@SuppressWarnings("unchecked")
		@Override
		public <T extends PsrObject> Class<T> getClassObject() {return (Class<T>) _class;}
	}
	
	protected Map<ModelData, Map<String, ? extends PsrObject>> _model =
		new EnumMap<>(ModelData.class);
	protected Map<CaseData, Map<String, ? extends PsrObject>> _case =
		new EnumMap<>(CaseData.class);
		
	public static final Map<String,PsrObject> EmptyPsrList = new HashMap<>(0);
	
	
	
	public Map<String,Organization> getOrganization() {return getModelData(ModelData.Organization);}
	public Map<String,ControlArea> getControlArea() {return getModelData(ModelData.ControlArea);}
	public Map<String,Substation> getSubstation() {return getModelData(ModelData.Substation);}
	public Map<String,Node> getNode() {return getModelData(ModelData.Node);}
	public Map<String,Load> getLoad() {return getModelData(ModelData.Load);}
	public Map<String,GeneratingUnit> getGeneratingUnit() {return getModelData(ModelData.GeneratingUnit);}
	public Map<String,SynchronousMachine> getSynchronousMachine() {return getModelData(ModelData.SynchronousMachine);}
	public Map<String,Line> getLine() {return getModelData(ModelData.Line);}
	public Map<String,Transformer> getTransformer() {return getModelData(ModelData.Transformer);}
	public Map<String,TransformerWinding> getTransformerWinding() {return getModelData(ModelData.TransformerWinding);}
	public Map<String,RatioTapChanger> getRatioTapChanger() {return getModelData(ModelData.RatioTapChanger);}
	public Map<String,Switch> getSwitch() {return getModelData(ModelData.Switch);}
	public Map<String,SwitchType> getSwitchType() {return getModelData(ModelData.SwitchType);}
	public Map<String,PrimeMover> getPrimeMover() {return getModelData(ModelData.PrimeMover);}
	public Map<String,ReactiveCapabilityCurve> getReactiveCapabilityCurve() {return getModelData(ModelData.ReactiveCapabilityCurve);}
	public Map<String,SeriesDevice> getSeriesReactor() {return getModelData(ModelData.SeriesReactor);}
	public Map<String,SeriesDevice> getSeriesCapacitor() {return getModelData(ModelData.SeriesCapacitor);}
	public Map<String,SwitchedShunt> getShuntReactor() {return getModelData(ModelData.ShuntReactor);}
	public Map<String,SwitchedShunt> getShuntCapacitor() {return getModelData(ModelData.ShuntCapacitor);}
	public Map<String,SVC> getSVC() {return getModelData(ModelData.SVC);}
	public Map<String,PhaseTapChanger> getPhaseTapChanger() {return getModelData(ModelData.PhaseTapChanger);}
	public Map<String,Exciter> getExciter() {return getModelData(ModelData.Exciter);}
	
	public Map<String, SwitchedShunt> getSwitchedShunt()
	{
		Map<String, SwitchedShunt> cap = getShuntCapacitor();
		Map<String, SwitchedShunt> reac = getShuntReactor();
		HashMap<String, SwitchedShunt> rv = 
				new HashMap<>(cap.size() + reac.size());
		rv.putAll(cap);
		rv.putAll(reac);
		return rv;
	}	

	public Map<String, SeriesDevice> getSeriesDevice()
	{
		Map<String, SeriesDevice> cap = getSeriesCapacitor();
		Map<String, SeriesDevice> reac = getSeriesReactor();
		HashMap<String, SeriesDevice> rv = 
				new HashMap<>(cap.size() + reac.size());
		rv.putAll(cap);
		rv.putAll(reac);
		return rv;
	}	

	
	
	public Map<String,BasecaseControlArea> getBasecaseControlArea() {return getCaseData(CaseData.ControlArea);}
	public Map<String,BasecaseLoad> getBasecaseLoad() {return getCaseData(CaseData.Load);}
	public Map<String,BasecaseGeneratingUnit> getBasecaseGeneratingUnit() {return getCaseData(CaseData.GeneratingUnit);}
	public Map<String,BasecaseSynchronousMachine> getBasecaseSynchronousMachine() {return getCaseData(CaseData.SynchronousMachine);}
	public Map<String,BasecaseNode> getBasecaseNode() {return getCaseData(CaseData.Node);}
	public Map<String,BasecaseSwitch> getBasecaseSwitch() {return getCaseData(CaseData.Switch);}

	
	@SuppressWarnings("unchecked")
	public <T extends PsrObject> Map<String,T> getModelData(ModelData type)
	{
		Map<String,T> rv = (Map<String, T>) _model.get(type); 
		return (Map<String, T>) ((rv == null) ? EmptyPsrList : rv);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends PsrObject> Map<String,T> getCaseData(CaseData type)
	{
		Map<String,T> rv = (Map<String, T>) _case.get(type); 
		return (Map<String, T>) ((rv == null) ? EmptyPsrList : rv);
	}
	
	public void readModel(File dir) throws IOException, ReflectiveOperationException
	{
		_model.clear();
		for (ModelData md : ModelData.values())
		{
			_model.put(md, readRecordsForType(dir, 
				md.getFileName(), md.getClassObject()));
		}
		for (CaseData cd : CaseData.values())
		{
			_case.put(cd, readRecordsForType(dir, cd.getFileName(), cd.getClassObject()));
		}
	}
	
	protected <T extends PsrObject> Map<String, T> readRecordsForType(File dir,
		String fname, Class<T> clobj) throws IOException, ReflectiveOperationException
	{
		File f = new File(dir, fname);
		if (!f.exists()) f = new File(dir, fname.toLowerCase());
		if (!f.exists()) return null;

		BufferedReader rdr = new BufferedReader(new FileReader(f));

		RecordReader r = new RecordReader();
		r.prepCSV(rdr);

		Map<String, T> rv = new HashMap<>();

		while (r.prepRec(rdr))
		{
			T no = clobj.newInstance();
			no.configure(r);
//			System.out.println(no.toString());
			rv.put(no.getID(), no);
		}

		return rv;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		String sdir = null;
		String sout = null;

		int narg = args.length;
		for (int i = 0; i < narg;)
		{
			String a = args[i++];
			if (a.startsWith("-"))
			{
				int idx = (a.charAt(1) == '-') ? 2 : 1;
				switch (a.substring(idx))
				{
				case "d":
				case "dir":
				case "directory":
					sdir = args[i++];
					break;
				case "o":
				case "output":
					sout = args[i++];
					break;
				case "h":
				case "help":
					showHelp(false);
				default:
					System.out.println("parameter " + a + " not understood");
					showHelp(true);
				}
			}
		}

		CsvMemoryStore rep = new CsvMemoryStore();
		if (sdir == null)
		{
			System.out.println("No input directory specified");
			showHelp(true);
		}
		rep.readModel(new File(sdir));
		Writer baseout =
			(sout == null) ? new OutputStreamWriter(System.out)
				: new FileWriter(sout);
		PrintWriter out = new PrintWriter(new BufferedWriter(baseout));
		rep.dump(out);
		out.flush();
		
	}

	public void dump(PrintWriter out)
	{
		for (Entry<ModelData, Map<String, ? extends PsrObject>> e : _model
				.entrySet())
		{
			Map<String, ? extends PsrObject> val = e.getValue();
			dumpList(out, val);
		}
		
		for (Entry<CaseData, Map<String,? extends PsrObject>> e : _case.entrySet())
		{
			Map<String, ? extends PsrObject> val = e.getValue();
			dumpList(out, val);
		}
	}
	
	protected void dumpList(PrintWriter out, Map<String, ? extends PsrObject> val)
	{
		if (val != null)
		{
			for (PsrObject o : val.values())
			{
				out.print('\t');
				out.println(o);
			}
		}
		
	}
	
	public static void showHelp(boolean err)
	{
		System.out.println("CsvMemoryStore --directory csv_file_directory --output file_name [ --help ]");
		System.exit(err?1:0);
	}	
	
}
