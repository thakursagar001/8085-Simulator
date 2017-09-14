import java.util.HashMap;

public class Debugger {
	public static int debug(String c,HashMap mainMemory,String[] registers,HashMap memory, boolean []flag,int breakpoint,int steppoint) {
		String[] main=c.split(" ");
		switch(main[0]) {
			case "run":
			case "r":		
							int i=1;
							while(i<=breakpoint) {
								if(mainMemory.containsKey(i)) {
									if(mainMemory.get(i)=="HLT") {
										return 100;
									}
									String line=(String) mainMemory.get(i);
									Execution_Commands e=new Execution_Commands();
									if(line.charAt(0)=='J') {
										int q=e.jumpStatements(line,flag,mainMemory);
										if(q==-1) {
											System.out.println("Error in line "+i);
											return -2;
										}
										else if(q!=0){
											i=q-1;
										}
									}
									else {
										 boolean y=e.checkCommands(line, registers, flag, memory);
										 if(!y) {
												System.out.println("Error in line "+i);
												return -2;
										 }
									}
								}
								else {
									return -2;
								}
								i++;
							}
							return 100;
							
							
			case "s":
			case "step":
				
							int j=steppoint;
							if(mainMemory.get(j)=="HLT") {
								return 100;
							}
							String line=(String) mainMemory.get(j);
							Execution_Commands e=new Execution_Commands();
							if(line.charAt(0)=='J') {
								int q=e.jumpStatements(line,flag,mainMemory);
								if(q==-1) {
									System.out.println("Error in line "+j);
									return -2;
								}
								else if(q!=0){
									i=q-1;
								}
							}
							else {
								 boolean y=e.checkCommands(line, registers, flag, memory);
								 if(!y) {
										System.out.println("Error in line "+j);
										return -2;
								 }
							}
							j++;
							return j;
							
			case "print":
			case "p":		
							if(main.length!=2) {
								return 0;
							}
							else {
								if(main[1].charAt(0)>='A' && main[1].charAt(0)<='L') {
									if(main[1].charAt(0)=='F' || main[1].charAt(0)=='G' ) {
										return 0;
									}
									else {
										if(main[1].charAt(0)=='H'  ) {
											System.out.println("value of "+main[1].charAt(0)+" --> "+ registers['F'-65]);
										}
										else if(main[1].charAt(0)=='L'  ) {
											System.out.println("value of "+main[1].charAt(0)+" --> "+ registers['G'-65]);
										}
										else 
											System.out.println("value of "+main[1].charAt(0)+" --> "+ registers[main[1].charAt(0)-65]);
										return 100;
									}
								}
								else if(main[1].length()==4) {
									if(memory.containsKey(main[1])) {
										System.out.println("value of "+main[1]+" --> "+ memory.get(main[1]));
									}
									else {
										return 0;
									}
								}
								else {
									return 0;
								}
							}
							return 100;
			case "quit":
			case "q":		return -1;
							
			case "help":	getHelp();
							return 100;
							
			default:
							System.out.println("wrong command");
							return 0;
				
		}
		
	}

	private static void getHelp() {
		System.out.println("1. break or b `line no`:- It will set break point on given line number.");
		System.out.println("2. run or r :- Run the program until it ends or breakpoint is encountered.");
		System.out.println("3. step or s :- It will run the program one instruction at a time.");
		System.out.println("4. print or p:- It prints the value of register or memory location. for ex p A will print the value of\n" + 
							"register A. p x2500 will print the value at memory location x2500 if any.");
		System.out.println("5. quit or q:- quits the debugger.");
		System.out.println("6. help:- will show all the commands of debugger.");
	}
}
