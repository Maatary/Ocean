package ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mindswap.pellet.ABox;
import org.mindswap.pellet.Node;
import org.mindswap.pellet.utils.ATermUtils;
//import org.protege.owl.portability.arguments.SWRLBuiltInArgument;
//import org.protege.owl.portability.arguments.SWRLBuiltInArgumentFactory;
import org.protege.owl.portability.swrl.arguments.SWRLBuiltInArgument;
import org.protege.owl.portability.swrl.arguments.SWRLBuiltInArgumentFactory;
import org.protege.owl.portability.exceptions.BuiltInException;
import org.protege.swrlapi.builtins.AbstractSWRLBuiltInLibrary;
import org.protege.swrlapi.builtins.temporal.SWRLBuiltInLibraryImpl;
import org.protege.swrlapi.exceptions.SWRLBuiltInLibraryException;

import ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin.CustomSWRLBuiltin.CustomSWRLFunction;

public class equals implements CustomSWRLFunction {

	
	/**
	 * Can have 2 up to 5 arguments hence >1 && <6
	 */
	@Override
	public boolean isApplicable(boolean[] boundPositions) {
		//Every position have to be bounded.
		if (boundPositions.length > 1 && boundPositions.length < 6) {
			for (int i = 0; i < boundPositions.length; i++) {
				if (!boundPositions[i])
					return false;
			}
			return true;
		}
		return false;//not the proper number of arguments
	}
	
	
	
	@Override
	public boolean apply(ABox abox, Node[] args) {
		
		AbstractSWRLBuiltInLibrary tempBuiltLib = new SWRLBuiltInLibraryImpl ();
		
		try {
			tempBuiltLib.reset();
			String calculatedDate                      = null;
			SWRLBuiltInArgumentFactory BuiltArgfactory = SWRLBuiltInArgumentFactory.create();
			Method method                              = SWRLBuiltInLibraryImpl.class.getMethod("equals", List.class);
			
			//System.out.println(method.toGenericString() + "\n is called with args: ");
			
			/*for (int i = 0; i < args.length; i++) {
				
				if (args[i] == null)
					System.out.println("null");
				else
					System.out.println(ATermUtils.getLiteralValue(args[i].getTerm()));				
			}*/
			//System.out.println("-----");
			
			List<SWRLBuiltInArgument> arguments = new Vector<SWRLBuiltInArgument>();
			
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
			String argAsString    = null;
			
			for (int i = 0; i < args.length; i++) {
				
				if (!args[i].isLiteral())
					return false;
				
				argAsString = ATermUtils.getLiteralValue(args[i].getTerm());
				
				if (!isSupposedToBeGranularityArg(argAsString)) {	
					argAsString = DateTime.parse(argAsString).withZone(DateTimeZone.getDefault()).toString(fmt);
				}
				
				arguments.add(BuiltArgfactory.createLiteralArgument(argAsString));
				
			}
			
			boolean builtinReturn = false;
			//System.out.println("The invoked \"equals\" BuiltInMethod returned: " + (builtinReturn = tempBuiltLib.invokeBuiltInMethod(method, null, "ruleName", "temporal", "equals", 0, false, arguments)));
			//System.out.println(">>>>>\n");
			
			return builtinReturn;
			
		} catch (SWRLBuiltInLibraryException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (BuiltInException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Check if the argument is supposed to be a granularity. 
	 * If we don't have a date then it has to be a granularity.
	 * We attempt to part an integer, that is the second letter of the dateTime, in case
	 * we have a minus before. If the letter is an int, we return false. Otherwise, 
	 * we get an exception and therefore return true. 
	 * @param granulArg
	 * @return
	 */
	private boolean isSupposedToBeGranularityArg(String granulArg) {
		
		try {
			
			Integer.parseInt(granulArg.substring(1, 2));
			
		} catch (NumberFormatException e) {
			
			return true;
		}
		return false;
	}

}
