package ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import org.mindswap.pellet.ABox;
import org.mindswap.pellet.Node;
import org.mindswap.pellet.utils.ATermUtils;
//import org.protege.owl.portability.arguments.SWRLBuiltInArgument;
import org.protege.owl.portability.swrl.arguments.SWRLBuiltInArgument;
//import org.protege.owl.portability.arguments.SWRLBuiltInArgumentFactory;
import org.protege.owl.portability.swrl.arguments.SWRLBuiltInArgumentFactory;
import org.protege.owl.portability.exceptions.BuiltInException;
import org.protege.swrlapi.builtins.AbstractSWRLBuiltInLibrary;
import org.protege.swrlapi.builtins.temporal.SWRLBuiltInLibraryImpl;
import org.protege.swrlapi.exceptions.SWRLBuiltInLibraryException;

import aterm.ATermAppl;


import static org.mindswap.pellet.utils.Namespaces.XSD;




public class add implements CustomSWRLBuiltin.CustomSWRLFunction {
	
	//static String XSD = "http://www.w3.org/2001/XMLSchema#";
	/**
	 * add(?t, "2002-02-09T10:20:00", 10, "Minutes");
	 * ?t can be bound or not. But the rest of the argument must be.
	 * 
	 */
	@Override
	public boolean isApplicable(boolean[] arg0) {
		return arg0.length == 4 && arg0[1] && arg0[2] & arg0[3];
	}

	/**
	 * !!!Only Literals arguments are manage (including variable over literals)
	 * TODO Support of individuals
	 */
	@Override
	public boolean apply(ABox abox, Node[] args) {
		
		
		
		//accepts add(unbound variable or bound, date or dateTime string, Literal integer, granularity string)
		// When a variable is unbound, you have a null value, otherwise you have a bound variable
		//!!!! We only deal with literals as of now !!!!
		if (/*args[0] != null || */!args[1].isLiteral() || !args[2].isLiteral() || !args[2].isLiteral()) return false;
		
		String dateTimeorDate = ATermUtils.getLiteralValue(args[1].getTerm());
		int count             = Integer.parseInt(ATermUtils.getLiteralValue(args[2].getTerm()));
		String granularity    = "";
		
		if (args[3].isLiteral()) 
			granularity    = ATermUtils.getLiteralValue(args[3].getTerm());
		else {
			granularity = args[3].getNameStr();
			granularity = granularity.substring(granularity.indexOf("#") + 1);
		}

		
		
		AbstractSWRLBuiltInLibrary tempBuiltLib = new SWRLBuiltInLibraryImpl ();

		try {
			
			tempBuiltLib.reset();
			String calculatedDate                      = null;
			SWRLBuiltInArgumentFactory BuiltArgfactory = SWRLBuiltInArgumentFactory.create();
			Method method                              = SWRLBuiltInLibraryImpl.class.getMethod("add", List.class);
			
			//System.out.println(method.toGenericString() + "\n is called with args: ");
			
			/*for (int i = 0; i < args.length; i++) {
				
				if (args[i] == null)
					System.out.println("null");
				else {
					if (i == 3) {
						if (args[i].isLiteral()) 
							System.out.println(ATermUtils.getLiteralValue(args[i].getTerm()));
						else {
							String temp = null;
							temp = args[i].getNameStr();
							temp = temp.substring(temp.indexOf("#") + 1);
							System.out.println(temp);
						}
					}
					else
						System.out.println(ATermUtils.getLiteralValue(args[i].getTerm()));
				}
				
			}*/
			//System.out.println("-----");
			
			
			List<SWRLBuiltInArgument> arguments = new Vector<SWRLBuiltInArgument>();
		
			//Is our first argument a value (from a variable or not) not ?
			if (args[0] != null ) {
				
				if (ATermUtils.isLiteral(args[0].getTerm())) {
					//SWRLVariableBuiltInArgument x = BuiltArgfactory.createVariableArgument("x");
					//x.setBound();
					//BuiltArgfactory.createVariableArgument(variableName)
					//System.out.println("The add builtin is called with a bounded value of: "+ ATermUtils.getLiteralValue(args[0].getTerm()));
					arguments.add(BuiltArgfactory.createLiteralArgument(ATermUtils.getLiteralValue(args[0].getTerm())));
				}
				else {
					System.err.println("temoral:add - Individual in builtins not yet supported");
					return false;
				}
			}
			else
				arguments.add(BuiltArgfactory.createUnboundVariableArgument("x"));
			
			arguments.add(BuiltArgfactory.createLiteralArgument(dateTimeorDate));
			arguments.add(BuiltArgfactory.createLiteralArgument(count));
			arguments.add(BuiltArgfactory.createLiteralArgument(granularity));
			
			boolean builtinReturn = false;
			//System.out.println("the invoked \"add\" BuiltInMethod returned: " + (builtinReturn = tempBuiltLib.invokeBuiltInMethod(method, null, "ruleName", "temporal", "add", 0, false, arguments)));
			
			if (args[0] == null) {
				//System.out.println(arguments.get(0).hasBuiltInResult() ? "The calculated date is: " + (calculatedDate = arguments.get(0).getBuiltInResult().toString()) : "no builtin result");	
				args[0] = abox.addLiteral(ATermUtils.makeTypedLiteral(calculatedDate, XSD + "dateTime"));
			}
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
	
	
	public static void main(String[] args) {
		
		boolean [] myboolarray = {false, true, true, true};
		
		System.out.println(new add().isApplicable(myboolarray));
		
	}

}
