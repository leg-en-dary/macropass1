import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class macro 
{
	public static void main(String[] args) throws IOException 
	{
		BufferedReader br = new BufferedReader(new FileReader("source.txt"));
		BufferedWriter mnt = new BufferedWriter(new FileWriter("mnt.txt"));
		BufferedWriter mdt = new BufferedWriter(new FileWriter("mdt.txt"));
		BufferedWriter arg = new BufferedWriter(new FileWriter("argument.txt"));

		LinkedHashMap <String,String> hash = new LinkedHashMap <String,String>();
		
		mnt.write("#\tMName\tMDTP\n");
		String line=null;
		String macroname=null;
		int mdtp=0,mntp=0,arg_ptr=0;
		boolean flag=false;
		
		while((line=br.readLine())!=null)
		{
			String parts[] = line.split("\\s+");
			if(parts[0].equals("MACRO"))
			{
				line=br.readLine();
				
				mdt.write(Integer.toString(mdtp)+"\t"+line+"\n");//enter name of macro and def in mdt and increment mdtp;
				mdtp++;
				
				String part[] = line.split("\\s+");
				macroname = part[0];			//first word is macro name
				
				mnt.write(Integer.toString(mntp)+"\t"+macroname+"\t"+Integer.toString(mdtp)+"\n");	//enter macro name from mdt in mnt and increment mdtp;
				mntp++;
				
				for(int i=1;i<part.length;i++)
				{
					part[i]=part[i].replaceAll("[&,]", "");		//remove & sign
					hash.put(part[i],"#"+Integer.toString(arg_ptr) );
					System.out.println(hash);
					arg_ptr++;
				}
				flag=true;
			}
			else if(parts[0].equals("MEND"))
			{
				flag=false;		//end of macro defination
				mdt.write(Integer.toString(mdtp)+"\tMEND\n");
			}
			else if(flag)				//reading macro defination
			{
				line=line.replaceAll("[&,]", "");
				//System.out.println(line);
				
				String part[] = line.split("\\s+");
				String add_line = Integer.toString(mdtp)+"\t"+part[0]+"\t"+hash.get("REG")+", ";
				mdtp++;
				if(part[2].equals("X"))
					add_line+=hash.get("X");
				else
					add_line+=hash.get("Y");
				
				mdt.write(add_line+"\n");
			}				
		}
		
		arg.write("#\tArgument_List\n");
		for(Map.Entry m : hash.entrySet())
		{
			arg.write(m.getValue()+"\t"+m.getKey()+"\n");
		}
		
		arg.close();
		mnt.close();
		mdt.close();
	}

}
