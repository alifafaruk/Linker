//update
import java.io.*;
import java.util.*;
import java.util.Map.Entry;


public class TwoPassLinker {
    static HashMap<Integer, Integer >  FinalAddresses = new HashMap<Integer, Integer>();
    static HashMap<Integer, String >  FinalAddressesError = new HashMap<Integer, String>();
    static ArrayList<String> UsessError = new ArrayList<String>();
    static ArrayList<String> DefError = new ArrayList<String>();
    static ArrayList<String> ExternalNotUse = new ArrayList<String>();
    static HashMap<Integer, String > externalNotOnChain = new HashMap<Integer, String >();
    
  public static  HashMap<Integer, Integer >  external = new HashMap<Integer, Integer>();

    static LinkedHashMap<String, Integer> symbolTable = new LinkedHashMap<String, Integer>();
    
    static ArrayList<Integer> ImEx = new ArrayList<Integer>();
    static int machineMax=200;
  //Mod Number and useList for that mod
	static LinkedHashMap<Integer, ArrayList<String>> ModUseList = new  LinkedHashMap<Integer, ArrayList<String>>();
	
	//Mod Number and list of texts
	static  LinkedHashMap<Integer, ArrayList<Integer>> ModText = new LinkedHashMap<Integer,  ArrayList<Integer>>();
	static  LinkedHashMap<Integer, ArrayList<String>> ModDef = new LinkedHashMap<Integer,  ArrayList<String>>();
	
	static ArrayList<String> symbolsMod = new ArrayList<String>();
    static HashMap<String, Integer> defSize = new HashMap<String, Integer>();


	 //Mod and base loc
	static LinkedHashMap<Integer, Integer> ModBaseLocation = new LinkedHashMap<Integer,  Integer>();
   
	public static void main(String[] args) throws Exception {
    	//Store symbol and which mod it is in
        HashMap<String, Integer > symbolsDefined = new HashMap<String, Integer>();
        
    	ArrayList<String> symbols = new ArrayList<String>();
    	ArrayList<String> deflist = new ArrayList<String>();
    	ArrayList<String> unusedDefList = new ArrayList<String>();
    	
    	//All use List
    	ArrayList<String> useList = new ArrayList<String>();
    	
    	
    	//ArrayList<Integer> symbolsLocation = new ArrayList<Integer>();
    	
    	
        //Check if defined or not
        LinkedHashMap<String,Integer> defMap = new LinkedHashMap<String,Integer>();        
       
          
        LinkedHashMap<String, String> SymbolerrorMessages= new LinkedHashMap<String, String>();
    	int baseLoc=0;
    	int symLoc=0;
    	int textCount=0;
    	int modSize=0;
    	Scanner passInput = new Scanner(System.in);
    	String val=passInput.next().trim();
    	 
    	int numMod= Integer.parseInt(val);
    	 int absoluteAddress=0;
    	int ModNum=0;
    	
    	
    	 
    	do {
    		  
        	ArrayList<Integer> usedNotDef = new ArrayList<Integer>();
    	//	ArrayList<Integer> sysMod = new ArrayList<Integer>();
    		String defList;
    		ModBaseLocation.put(ModNum, baseLoc);
    	if(numMod==0) {
    		break;
    	}
    	String values=passInput.nextLine().trim();
    	 
    	
    	if(values.compareTo("")==0) { 
    		defList=passInput.next().trim();
    		 
    	}
    	else {
    		
    		defList=values;
    	}
    	int useCount= Integer.parseInt(defList);
     
    	if(useCount>0) {
    	for(int i=0; i<useCount;i++) {
    	
    		String nextDef=passInput.next().trim();
    		 
    		if (checkTable(nextDef,symbols)!=false) {
    			SymbolerrorMessages.put(nextDef, "Error: This variable is multiply defined; first value used.");
    			passInput.nextLine();
    		}
    		else if((symbolTable.containsKey(nextDef))) {
    			passInput.nextLine();
    		}
    		else {
    			 
    			int defVal=Integer.parseInt(passInput.next().trim());
	    		symbols.add(nextDef);
	    		deflist.add(nextDef);
	    		
	    		//sym, mod num
	    		symbolsDefined.put(nextDef, ModNum);
	    		
	    		
	    		
	    		int relativeLocation= defVal;
	    		symLoc= baseLoc + relativeLocation;
	    		defMap.put(nextDef,ModNum);
	    		if(symLoc>machineMax) {
	    			symbolTable.put(nextDef, 199);
	    			SymbolerrorMessages.put(nextDef, "The absolute address exceeds the size of the machine. 199 used" );
	    		}
	    		else {
	    		symbolTable.put(nextDef, symLoc);
	    		}
	    		
	    		symbolsMod.add(nextDef);
	    		ModDef.put(ModNum,symbolsMod );
	    		//usedNotDef.add(nextDef);
    		}
    	
    	}
    	}
    	
        ArrayList<Integer > usesRepeat = new ArrayList< Integer>();
    	String useLine= passInput.next().trim();
    	
    	int useLineLength=Integer.parseInt(useLine);
    	ArrayList<String> ModUse = new ArrayList<String>();
    	for(int i=0;i<useLineLength;i++){
    		String val1=passInput.next();
    	
    		int in=Integer.parseInt(passInput.next());
    		//If multiple symbols are listed as used in the same instruction, print an error message and ignore all but the last usage given
    		
    		if(usesRepeat.contains(in) && ModUse.indexOf((Integer.toString(absoluteAddress)))!=-1) {
    			int count=ModUse.indexOf(Integer.toString(absoluteAddress));
    			
    			UsessError.add("Warning: The use value " + val1+" int module " + ModNum +" was used in the same instruction as another" + ". Ignore but the last usage" );
    			ModUse.remove(count);
    			ModUse.remove(count-1);
    			
    			
    			usesRepeat.add(in);
    		}
    		else {
	    		usesRepeat.add( in);
	    		absoluteAddress=in+baseLoc;
	    		useList.add(val1);
	    		ModUse.add(val1);
	    	
	    		ModUse.add(String.valueOf(absoluteAddress));
	    		
    	}
    }
    	
    	ModUseList.put(ModNum, ModUse);
    	
 
    	check(ModNum,ModUseList.get(ModNum),useList);
    	int programText =Integer.parseInt(passInput.next().trim());
    	
    	 
    	baseLoc=baseLoc+programText;
    	ArrayList<Integer> textList = new ArrayList<Integer>();
    	modSize=programText;
    	for (int j = 0; j < programText; j++){
    		
    		int an=Integer.parseInt(passInput.next().trim());
    		textList.add(an);
    		textList.add(textCount);
    	  	 
    	  	textCount++;
    	}
    
    	ModText.put(ModNum,textList );
     
    	
    	 
    	ModNum++;
    	
    	}while(ModNum<numMod);
    
    	printSystemTable(symbolTable, SymbolerrorMessages);
   
   
      
    for(int j=0; j<useList.size()-1;j++) {
   
    	if(deflist.size()>j ) {
    		if(useList.contains(deflist.get(j))==true) {}
    	else {	 
    		
    		unusedDefList.add(deflist.get(j));
    	}
  
    }
    	//unusedDefList.
    }
    
   /*****************************************************************************Memory Map*************************************************************************************************/
    
    System.out.println("\nMemory Map");
    //Each Mod
    int count=0;

    for (Integer mods : ModText.keySet()) {
    	 
    	   LinkedHashMap<Integer, Integer> ModTextPostion = new LinkedHashMap<Integer, Integer>();

    	    int counter=0;
  HashMap<Integer, Integer >  allE = new HashMap<Integer, Integer>();
        ArrayList<String> usedInMod=ModUseList.get(mods);
      int baseMod=  ModBaseLocation.get(mods);
      
    	//System.out.println(mods);
    	int lastDigit=0;
    	int address=0;
    	int finalAddress=0;
    	ArrayList<Integer> ModInstruction=ModText.get(mods);
    	for (int x=0;x<ModInstruction.size();x=x+2) {
    		
    		ModTextPostion.put(x, ModInstruction.get(x));
    		 lastDigit=getLastDigit(ModInstruction.get(x));
    		 
    		 address= getAddress(ModInstruction.get(x), lastDigit);
    		
    		if(lastDigit==1) {
    		//immediate; Unchanged	
    			 
    			if(usedInMod.contains(Integer.toString(baseMod+counter+addressMod(address)))){
    				allE.put(ModInstruction.get(x+1),address);
    				ImEx.add(address);
    				ImEx.add(ModInstruction.get(x+1));
    				FinalAddressesError.put(ModInstruction.get(x+1), "Error: Immediate address on use list; treated as External.");
    			}
    			else {
    			finalAddress=address;
    			 
    			FinalAddresses.put(count, finalAddress);
    		}
    		}
    		else if(lastDigit==2) {
    			//absolute; Unchanged	
    			finalAddress=address;
    			//System.out.println(count +": " +finalAddress);
    			FinalAddresses.put(count, finalAddress);
    		}
    		else if(lastDigit==3) {
    			//Relative; Relocated
	    	int baseLocation=ModBaseLocation.get(mods);
	    	finalAddress=address+baseLocation;
	    	FinalAddresses.put(count, finalAddress);
    		}
    		else if(lastDigit==4) {
    			int externalAddress=addressMod(address);
    			//External; Resolved
    			int exceeds=0;
    			 
    			 
	    			 for(int i=0;i<usedInMod.size();i=i+2) {
	 
	 	    			 if(ModInstruction.contains(Integer.parseInt(usedInMod.get(i+1)))){
	 	    				 
	 	    				exceeds++;}
	    			 }
	 	    			 
    			 if(exceeds==0) {
    				 System.out.println("Entered");
    				 finalAddress=address;
 	    			 FinalAddresses.put(count, finalAddress);
 	    			FinalAddressesError.put(ModInstruction.get(x+1), "external address is not on a use list. Treated as immediate value");
    			 }
    			 else {	 
    				 allE.put( ModInstruction.get(x+1),address);
    				 external.put(ModInstruction.get(x+1), address);
    				 
    			 }
    			
    	}
    
    
    		
    		
    		
    		else {
    			//Error
    		}
    		
    		count++;
    		counter++;
    	}
    	Set<Integer> keys= allE.keySet();
    	
		ArrayList<Integer> Ekeys= new ArrayList<Integer>();
        for(Integer key: keys){
        	Ekeys.add(key);
        }
    		
    		ArrayList<String> def=ModDef.get(mods);
		if(usedInMod.size()!=0) {
    			for(int j=0;j<usedInMod.size();j=j+2) {
    				
    			 
    				for(int u=0;u<Ekeys.size();u++) {
    					if(def!=null &&def.contains(usedInMod.get(j))==false) {
        					FinalAddressesError.put(Ekeys.get(u), "Error: " + usedInMod.get(j) +" is not defined; zero used.");
        					FinalAddresses.put(Ekeys.get(u), finalAddress);
        					int relativeAddress= allE.get(Ekeys.get(u));
        					int val0=0;
        					int firstDigit=(Integer)relativeAddress/1000;
							finalAddress=1000*firstDigit+val0;
							external.remove(Ekeys.get(u));
							FinalAddresses.put(Ekeys.get(u), finalAddress);
        				}
    					
    					else if(!(symbolTable.containsKey(((usedInMod.get(j))))) ){
    						FinalAddressesError.put(Ekeys.get(u), "Error: " + usedInMod.get(j) +" is not defined; zero used.");
    						FinalAddresses.put(Ekeys.get(u), finalAddress);
        					int relativeAddress= allE.get(Ekeys.get(u));
        					 
        					int val0=0;
        					int firstDigit=(Integer)relativeAddress/1000;
							finalAddress=1000*firstDigit+val0;
							external.remove(Ekeys.get(u));
							FinalAddresses.put(Ekeys.get(u), finalAddress);
    					}
    					else if(Ekeys.get(u)==Integer.parseInt(usedInMod.get(j+1))) {
    				
		    				int relativeAddress= allE.get(Ekeys.get(u));
		    				 
		    				if(addressMod(relativeAddress)==777) {
		    					int val0=symbolTable.get(usedInMod.get(j));
								int firstDigit=(Integer)relativeAddress/1000;
								finalAddress=1000*firstDigit+val0;
								external.remove(Ekeys.get(u));
								FinalAddresses.put(Ekeys.get(u), finalAddress);
		    				}
		    			 
		    				int val0=symbolTable.get(usedInMod.get(j));
							int firstDigit=(Integer)relativeAddress/1000;
							finalAddress=1000*firstDigit+val0;
							external.remove(Ekeys.get(u));
							FinalAddresses.put(Ekeys.get(u), finalAddress);
						 
							if(ImEx.contains(relativeAddress)) {
								int k=ImEx.indexOf(relativeAddress);
							correctAddress(allE,usedInMod.get(j),relativeAddress,addressMod(relativeAddress),mods,baseMod,0,ModTextPostion,1,k+1);
							}
							else {
								correctAddress(allE,usedInMod.get(j),relativeAddress,addressMod(relativeAddress),mods,baseMod,0,ModTextPostion,0,0);

							}
    				}
    				
    				}
     
    				}
    			 
    			}
		else {
		}
			
    }
    		 

		
	    	if(external.isEmpty()==false) {
	    		
	    		for(Integer key: external.keySet()) {
	    			int ad=external.get(key);
	    			FinalAddressesError.put(key, "Error: E type address not on use chain; treated as I type.");
	    			FinalAddresses.remove(key);
	    			FinalAddresses.put(key, ad);
	    		}
	    	}
	   

   
    
    Set<Integer> set= FinalAddressesError.keySet();
     
    for (Integer mod: FinalAddresses.keySet()){
    	 
        String key = mod.toString();
        if(set.contains(mod)){
        	 String value = FinalAddresses.get(mod).toString();  
             System.out.println(mod + ":" + value + " " +FinalAddressesError.get(mod)) ;  
        }
        else {
        String value = FinalAddresses.get(mod).toString();  
        System.out.println(mod + ":" + value);  
	}
    }
    checkSymbols(defMap,useList);
    usesError(UsessError);
    checkDefList(modSize);

   }
	
	public static void check(int modNum,ArrayList<String>ModUse, ArrayList<String> usedNotDef) {
		for(int i=0;i<ModUse.size();i=i+2) {
			int  baseLoc=ModBaseLocation.get(modNum);
	    	 if((usedNotDef.contains(ModUse.get(i))==false)) {
	    		 
	    		 
	    		 symbolTable.put(ModUse.get(i), (baseLoc+Integer.parseInt(ModUse.get(i+1))+1));
	    		 
	    	 }
	     }
	}
    
    
    public static boolean checkTable(String sym,ArrayList<String> arr) {
    	if(arr.isEmpty()) {
    		return false;
    	}
    	
    	if(arr.indexOf(sym)==-1) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    public static int getLastDigit(int num) {	
    	
    		int last=num%10;
    		
    	return last;
    }
    

    public static ArrayList<Integer> HmaptoArray(String[] list){
    	 ArrayList<Integer> values= new ArrayList<Integer>();
    	 int i=0;
    	 try {
    	 while(list[i]!=null) {
    		 
    		 values.add(Integer.parseInt(list[i]));
    		 i++;
    	 }
    	 }catch(Exception ex) {}
    	 return values;
    }
    
    public static int getAddress(int num, int lastDigit) {
    	int finalValue= num-lastDigit;
    	finalValue=finalValue/10;
    	return finalValue;
    }
    public static int getFirst(int num) {
    	int finalValue= (int)num/1000;
    	finalValue=finalValue*1000;
    	return finalValue;
    }
    
    public static void print(File file) throws Exception {
    	BufferedReader br = new BufferedReader(new FileReader(file));
    	 String line = null;
    	 //System.out.println("ENTERING");
    	 while ((line = br.readLine()) != null) {
    		// System.out.println("ENTERING");
    	   System.out.println(line);
    	 }
    }
    
    public static void secondPass() {
    	System.out.println("\nMemory Map");
    	int baseAddress=0;
    	
    }
   
    public static void printSystemTable(LinkedHashMap<String, Integer> map,LinkedHashMap<String, String> errors ) {
    	 
    	Set<String>  errorKey= errors.keySet();
    	System.out.println("Symbol Table");
    	
    	for (String mod: map.keySet()){
    		
            String key = mod.toString();
            if(errorKey.contains(key)) {
            	
            String value = map.get(mod).toString();  
            System.out.println(key + "=" + value + " " +errors.get(key));  
} 
            else {
            	 String value = map.get(mod).toString();  
                 System.out.println(key + "=" + value);
            }
    	}
    }
    
    
    public static void checkSymbols(LinkedHashMap<String, Integer> defMap, ArrayList<String> unusedDefList) {
    	//System.out.println("Checking if warning " + unusedDefList.size() );
    	ArrayList<String> notUsed= new ArrayList<String>();
    	 for ( String key : defMap.keySet() ) {
    			//System.out.println("Comparing " + key);
    			if( unusedDefList.contains(key)!=true) {
	    	        	System.err.println("Warning: "+ key+ " was defined in module " + defMap.get(key)+ " but was never used.");
	    	        }
	    	    	
	    	    	
    	  
    	    }
    }
    
    public static void usesError(ArrayList<String> repeat) {
    	for(int i=0; i<repeat.size();i++) {
    		System.err.println(repeat.get(i));
    	}
    }
    
    public static void defError() {
    	for(int i=0; i<DefError.size();i++) {
    		System.err.println(DefError.get(i));
    	}
    }
    public static int addressMod(int address) {
    	int finalAd=(int)address/1000 *1000;
    	 
    	finalAd=address-finalAd;
     
    	return finalAd;
    	
    }
    
    
  
    public static void correctAddress(HashMap<Integer, Integer > value , String use , int instruction, int actualAddress, int modBaseValue,int baseLocation, int updateAt,HashMap<Integer, Integer > textPos,int immediate,int loc ) {
    	//ArrayList<String> modpos=ModUseList.get(modBaseValue);
    	
    	
    	//System.out.println("LIST PRINTING"+ list);
    	ArrayList<Integer> list=ModText.get(modBaseValue);
   	 if(immediate==1 && actualAddress!=777) {
   		  int Number=list.get(actualAddress+1);
   		  int addressToUpdate=Number;
   		  
   		
   		    int nextAddress=addressMod(addressToUpdate);
   		    FinalAddressesError.put(actualAddress+1, "Error: Immediate address on use list; treated as External.");
   		    int val0=symbolTable.get(use);
   			int firstDigit=(Integer)(instruction)/1000;
   			int finalAddress=1000*firstDigit+val0;
   			FinalAddresses.put((actualAddress+baseLocation), finalAddress);
   			external.remove((actualAddress+baseLocation));
   			 if(nextAddress!=777) {
   			correctAddress(value ,use,addressToUpdate ,nextAddress,modBaseValue, baseLocation,actualAddress,textPos,4, loc);
   			 }
   	 }
   
    	 else {
  if(actualAddress!=777) {
    if(!(value.containsKey(baseLocation+actualAddress))) {
    	 
    	return;
    }
    int position=0;
    int counter=0;
    boolean flag=false;
  //  System.out.println(getAddress(list.get(i),getLastDigit(list.get(i))));
    for(int i=0; i<list.size();i=i+2) {
    
    	 int value1= (getAddress(list.get(i),getLastDigit(list.get(i))));
    	 //System.out.println("THE ACTUAL ADDRESS" + actualAddress);
    	 if(actualAddress==counter) {
    	if(value.get((baseLocation+actualAddress))==value1) {
    		position=list.get(i+1);
    		flag=true;
    		break;
    	}
 
    	
    	 }
    	   	counter++;
    }
    if(flag==false) {
    	return;
    }
    
     
    if(!(value.containsKey(position))) {
    	return;
    }
    int addressToUpdate=value.get((position));
 
  
    int nextAddress=addressMod(addressToUpdate);
    
    
    //System.out.println( "   "+nextAddress);
    int val0=symbolTable.get(use);
	int firstDigit=(Integer)addressToUpdate/1000;
	int finalAddress=1000*firstDigit+val0;
 
  
    FinalAddresses.put((actualAddress+baseLocation), finalAddress);
  
    
    external.remove((actualAddress+baseLocation));
    correctAddress(value ,use,addressToUpdate ,nextAddress,modBaseValue, baseLocation,actualAddress,textPos,4,0);
    
  }
  

	}
    }  
    public static void checkDefList(int modSize) {
    	 
    	for(int i=0; i<defSize.size();i++) {
    		for (String mod: defSize.keySet()){
    			if(defSize.get(mod)>modSize) {
    				DefError.add("Aaddress appearing in the definition of "+ mod+" exceeds the size of the module. Treated as as 0");
    			}
    		}
    	}
    }
     
   
    
}
