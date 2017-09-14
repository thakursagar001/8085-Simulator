import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

	
	public static void print(String registers[],boolean [] flag,HashMap<String, String> memory) {
		System.out.println("Registers");
		for(String q:registers) {
			System.out.print(q+" ");
		}
		System.out.println("\nFlags");
		for(boolean q:flag) {
			System.out.print(q+" ");
		}
		System.out.println("\nMemories");
		for(String key:memory.keySet()) {
			System.out.print(key+"->"+memory.get(key)+"   ");
		}
		System.out.println();
	}
	public static void main(String[] args) throws IOException {
		String registers[]=new String[7] ;
		boolean [] flag=new boolean[8];
		for(int i=0;i<7;i++) {
			registers[i]="00";
		}
		HashMap<String, String> memory=new HashMap();
		HashMap<Integer,String> mainMemory=new HashMap();
		String file_Name;
		switch(args.length) {
			case 0:
				BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
				file_Name="abc.txt";
				FileWriter fw=new FileWriter("abc.txt");
				String text="";
				String newLine = System.getProperty("line.separator");
				while(!text.equals("HLT")) {
					text=br.readLine();
					
					fw.write(text);
					fw.write(newLine);
				}
				fw.close();
				br.close();
				break;
			
			default: 
				if(args.length>2) {
					System.out.println("Invalid Arguements");
					return;
				}
				else {
					file_Name=args[0];
				}
		}
		
		BufferedReader br=new BufferedReader(new FileReader(file_Name));
		String line;
		Execution_Commands e=new Execution_Commands();
		int i=1;
		while((line=br.readLine())!=null ) {
			mainMemory.put(i, line);
			i++;
		}
		
		if(args.length==2) {
			Debugger db=new Debugger();
			if(args[1].equals("--debugger")) {
				Scanner input=new Scanner(System.in);
				int check=0;
				int breakpoint=0;
				int steppoint=1;
				while(check!=-1) {
					String c=input.nextLine();
					String[] main=c.split(" ");
					if(main[0].equals("b")||main[0].equals("break")) {
						try {
							breakpoint=Integer.parseInt(main[1]);
							steppoint=1;
							continue;
						}catch(Exception ee) {
							System.out.println("Invalid Line Number");
							continue;
						}
					}
					check=Debugger.debug(c,mainMemory,registers,memory,flag,breakpoint,steppoint);
					if(c.contains("s")|| c.contains("step")) {
						steppoint=check;
					}
					else {
						steppoint=1;
						
					}
					if(!(c.contains("p")|| c.contains("print")|| c.contains("q")|| c.contains("quit") )) {
						print(registers,flag,memory);
					}
					if(check==0) {
						System.out.println("Invalid command\nplease use help command for any help");
					}
					if(check==-2) {
						System.out.println("Error in code");
						break;
					}
					
					
				}
				
			}
			else {
				System.out.println("Invalid Command");
			}
		}
		
		else {
			i=1;
			while(mainMemory.containsKey(i)) {
				line=mainMemory.get(i);
				int q=0;
				boolean y;
				if(line.equals("HLT")) {
					break;
				}
				if(line.charAt(0)=='J') {
					q=e.jumpStatements(line,flag,mainMemory);
					if(q==-1) {
						System.out.println("Error in line "+i);
					}
					else if(q!=0){
						i=q-1;
					}
					
				}
				else {
					 y=e.checkCommands(line, registers, flag, memory);
					 if(!y) {
							System.out.println("Error in line "+i);
					 }
				}
				
				
				i++;
			}
			print(registers,flag,memory);
		}
		
		
		
	}

}
