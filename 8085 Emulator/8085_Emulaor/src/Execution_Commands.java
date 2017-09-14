import java.util.HashMap;

public class Execution_Commands {
	public static boolean checkCommands(String line,String []registers,boolean []flag,HashMap<String, String> memory) {
		
	
		String [] line_Words=line.split(" ");
		if(line_Words.length==3) {
			if(line_Words[1].equals("H")) {
				line_Words[1]="F";
			}
			if(line_Words[1].equals("L")) {
				line_Words[1]="G";
			}
			if(line_Words[2].equals("H")) {
				line_Words[2]="F";
			}
			if(line_Words[2].equals("L")) {
				line_Words[2]="G";
			}
		}
		else if(line_Words.length==2){
			if(line_Words[1].equals("H")) {
				line_Words[1]="F";
			}
			if(line_Words[1].equals("L")) {
				line_Words[1]="G";
			}
		}
		
		try {
			switch(line_Words[0]) {
				//Data Exchange Commands
				case "MOV": 
							if(line_Words.length!=3) {
								return false;
							}
							else {
								
								//Validating Commands
								if(!((line_Words[1].charAt(0)>='A' && line_Words[1].charAt(0)<='G') || line_Words[1].equals("M"))) {
									return false;
								}
								
								if(!((line_Words[2].charAt(0)>='A' && line_Words[2].charAt(0)<='G') || line_Words[2].equals("M"))) {
									return false;
								}
								
								
								//Assigning value
								if(!(line_Words[1].equals("M") || line_Words[2].equals("M"))) {
									registers[line_Words[1].charAt(0)-65]=registers[line_Words[2].charAt(0)-65];
								}
								
								
								else if(line_Words[1].equals("M")) {
									String m=registers[5]+registers[6];
									int q=line_Words[2].charAt(0)-'A';
									memory.put(m, registers[q]);
								}
								else if(line_Words[2].equals("M")) {
									String m=registers[5]+registers[6];
									if(memory.containsKey(m)) {
										registers[line_Words[1].charAt(0)-'A']=memory.get(m);
									}
									else {
										System.out.println("Invalid Memory Access");
										return false;
									}
								}
							}
							break;
				case "MVI": 
							if(line_Words.length!=3) {
								return false;
							}
							else {
								try {
									if(line_Words[1].equals("M")) {
										String m=registers[5]+registers[6];
										int q=(int)Long.parseLong(line_Words[2],16);
										String hex_value=line_Words[2];
										if(hex_value.length()<=2) {
											memory.put(m,hex_value);
										}
										else {
											if(hex_value.length()==3) {
												hex_value="0"+hex_value;
											}
											memory.put(m,hex_value.substring(2, 4));
											m=Integer.toHexString((int)Long.parseLong(m,16)+1);
											memory.put(m,hex_value.substring(0, 2));
										}
										
									}
									else if(line_Words[1].charAt(0)>='A' && line_Words[1].charAt(0)<='G') {
										String hex_value=line_Words[2];
										registers[line_Words[1].charAt(0)-65]=hex_value.substring(hex_value.length()-2);
									}
									else {
										return false;
									}
								}
								catch(Exception e) {
									return false;
								}
							}
							break;
				case "LXI": 
							if(line_Words.length!=3) {
								return false;
							}
							try {
								if(line_Words[2].length()!=4) {
									System.out.println("INVALID DATA ");
									return false;
								}
								
								switch(line_Words[1]){
									case "B":
												registers[1]=line_Words[2].substring(0, 2);
												registers[2]=line_Words[2].substring(2, 4);
												break;
									case "D":	
												registers[3]=line_Words[2].substring(0, 2);
												registers[4]=line_Words[2].substring(2, 4);
												break;
									case "F": 
												registers[5]=line_Words[2].substring(0, 2);
												registers[6]=line_Words[2].substring(2, 4);
												break;
									default:	return false;
											
								}
							}
							catch(Exception e) {
								return false;
							}
							break;
				case "LDA": 
							if(line_Words.length!=2) {
								return false;
							}
							if(line_Words[1].equals("M")) {
								String m=registers[5]+registers[6];
								if(memory.containsKey(m)) {
									registers[0]=memory.get(m);
								}
								else {
									return false;
								}
							}
							else {
								try {
									 if(line_Words[1].charAt(0)>='A' && line_Words[1].charAt(0)<='G') {
										 int q=line_Words[1].charAt(0)-'A';
										registers[0]=registers[q];
									}
									else {
										return false;
									}
								}
								catch(Exception e) {
									return false;
								}
							}
							break;
				case "STA": 
							if(line_Words.length!=2) {
								return false;
							}
							if(line_Words[1].equals("M")) {
								String m=registers[5]+registers[6];
								memory.put(m, registers[0]);
							}
							else {
								try {
									
									String m=line_Words[1];
									if(m.length()!=4) {
										System.out.println("Invalid Data");
										return false;
									}
									Long.parseLong(m);
									memory.put(m, registers[0]);
								}
								catch(Exception e) {
									return false;
								}
							}
							break;
				case "LHLD": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String hex=Integer.toHexString((int)Long.parseLong(line_Words[1],16)+1);
								if(memory.containsKey(line_Words[1]) && memory.containsKey(hex)) {
									registers[6]=memory.get(line_Words[1]);
									registers[5]=memory.get(hex);
								}
								else {
									return false;
								}
							}
							
							break;
				case "SHLD": 
							if(line_Words.length!=2) {
								return false;
							}
							try {
								Long.parseLong(line_Words[1],16);
								String hex=Integer.toHexString((int)Long.parseLong(line_Words[1])+1);
								memory.put(line_Words[1], registers[6]);
								memory.put(hex, registers[5]);
							}
							catch(Exception e) {
								return false;
							}
							break;
				case "STAX": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String m;
								switch(line_Words[1]){
									case "B":
												m=registers[1]+registers[2];
												break;
									case "D":	
												m=registers[3]+registers[4];
												break;
									case "F": 
												m=registers[5]+registers[6];
												break;
									default:	return false;
										
								}
								memory.put(m, registers[0]);
							}
							
							break;
				case "LDAX": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String m;
								switch(line_Words[1]){
									case "B":
												m=registers[1]+registers[2];
												break;
									case "D":	
												m=registers[3]+registers[4];
												break;
									case "F": 
												m=registers[5]+registers[6];
												break;
									default:	return false;
										
								}
								if(memory.containsKey(m)) {
									registers[0]=memory.get(m);
								}
								else {
									return false;
								}
							}
							break;
				case "XCHG": 
							if(line_Words.length!=1) {
								return false;
							}
							String temp=registers[5];
							registers[5]=registers[3];
							registers[3]=temp;
							temp=registers[6];
							registers[6]=registers[4];
							registers[4]=temp;
							break;
							
							
				//Arithmetic Commands
				case "ADD": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String f=registers[0].substring(registers[0].length()-1);
								String s;
								int accumulator=(int)Integer.parseInt(registers[0], 16);
								int value=0;
								if(line_Words[1].equals("M")) {
									String m=registers[5]+registers[6];
									if(memory.containsKey(m)) {
										value=(int)Long.parseLong(memory.get(m),16);
										s=memory.get(m);
										s=s.substring(s.length()-1);
									}
									else {
										return false;
									}
								}
								else if(line_Words[1].charAt(0)>='A' && line_Words[1].charAt(0)<='G') {
									value=(int)Long.parseLong(registers[line_Words[1].charAt(0)-'A'],16);
									s=registers[line_Words[1].charAt(0)-'A'];
									s=s.substring(s.length()-1);
								}
								else {
									return false;
								}
								int sum=accumulator+value;
								String ans=Integer.toHexString(sum);
								if(ans.length()<=1) {
									registers[0]="0"+ans;
								}
								else {
									registers[0]=ans.substring(ans.length()-2);
								}
								int num=Integer.parseInt(registers[0]);
								setFlag(ans,f,s,num,flag);
								
							}
							break;
				case "ADI": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String f=registers[0].substring(registers[0].length()-1);
								String s;
								int accumulator=(int)Integer.parseInt(registers[0], 16);
								int value;
								if(line_Words.length<=2) {
									value=Integer.parseInt(line_Words[1],16);
									s=line_Words[1].substring(line_Words[1].length()-1);
									int sum=accumulator+value;
									String ans=Integer.toHexString(sum);
									if(ans.length()<=1) {
										registers[0]="0"+ans;
									}
									else {
										registers[0]=ans.substring(ans.length()-2);
									}
									int num=Integer.parseInt(registers[0],16);
									setFlag(ans,f,s,num,flag);
								}
								else {
									return false;
								}
							}
							break;
				case "SUB": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String f=registers[0].substring(registers[0].length()-1);
								String s;
								int accumulator=(int)Integer.parseInt(registers[0], 16);
								int value=0;
								if(line_Words[1].equals("M")) {
									String m=registers[5]+registers[6];
									if(memory.containsKey(m)) {
										value=(int)Long.parseLong(memory.get(m),16);
										s=memory.get(m).substring(memory.get(m).length()-1);
									}
									else {
										return false;
									}
								}
								else if(line_Words[1].charAt(0)>='A' && line_Words[1].charAt(0)<='G') {
									value=(int)Long.parseLong(registers[line_Words[1].charAt(0)-'A'],16);
									s=registers[line_Words[1].charAt(0)-'A'];
									s=s.substring(s.length()-1);
								}
								else {
									return false;
								}
								int sum=accumulator-value;
								String ans=Integer.toHexString(sum);
								if(ans.length()<=1) {
									registers[0]="0"+ans;
								}
								else {
									registers[0]=ans.substring(ans.length()-2);
								}
								int num=Integer.parseInt(registers[0],16);
								setFlag1(ans,f,s,num,flag,sum);
								
							}
							break;
				case "INR": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								if(line_Words[1].equals("M")) {
									String m=registers[5]+registers[6];
									if(memory.containsKey(m)) {
										int value=(int)Long.parseLong(memory.get(m),16);
										value++;
										String value1=Integer.toHexString(value);
										memory.put(m, value1.substring(value1.length()-2));
										
									}
								}
								else if(line_Words[1].charAt(0)>='A' && line_Words[1].charAt(0)<='G') {
									int value=(int)Long.parseLong(registers[line_Words[1].charAt(0)-'A'],16);
									value++;
									String value1=Integer.toHexString(value);
									registers[line_Words[1].charAt(0)-'A']=value1.substring(value1.length()-2);
								}
								else {
									return false;
								}
							}
							break;
				case "DCR": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String f,s="1";
								if(line_Words[1].equals("M")) {
									String m=registers[5]+registers[6];
									if(memory.containsKey(m)) {
										int value=(int)Long.parseLong(memory.get(m),16);
										value--;
										f=memory.get(m).substring(memory.get(m).length()-1);
										memory.put(m, Integer.toHexString(value));
										
									}
									else {
										return false;
									}
								}
								else if(line_Words[1].charAt(0)>='A' && line_Words[1].charAt(0)<='G') {
									int value=(int)Long.parseLong(registers[line_Words[1].charAt(0)-'A'],16);
									value--;
									registers[line_Words[1].charAt(0)-'A']=Integer.toHexString(value);
									String y=registers[line_Words[1].charAt(0)-'A'];
									f=y.substring(y.length()-1);
									setFlag1(y,f,s,Integer.parseInt(y,16),flag,Integer.parseInt(y,16));
								}
								else {
									return false;
								}
							}
							break;
				case "INX": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String value="";
								int x;
								switch(line_Words[1]){
									case "B":
												value=registers[1]+registers[2];
												 x=Integer.parseInt(value,16);
												 x++;
												value=Integer.toHexString(x);
												if(value.length()==1 || value.length()==0) {
													value="000"+value;
												}
												else if(value.length()==2) {
													value="00"+value;
												}
												else if(value.length()==3) {
													value="0"+value;
												}
												registers[1]=value.substring(0, 2);
												registers[2]=value.substring(2,4);
												break;
									case "D":	
												value=registers[3]+registers[4];
												 x=Integer.parseInt(value,16);
												 x++;
												value=Integer.toHexString(x);
												if(value.length()==1 || value.length()==0) {
													value="000"+value;
												}
												else if(value.length()==2) {
													value="00"+value;
												}
												else if(value.length()==3) {
													value="0"+value;
												}
												registers[3]=value.substring(0, 2);
												registers[4]=value.substring(2,4);
												break;
									case "F": 
												value=registers[5]+registers[6];
												 x=Integer.parseInt(value,16);
												 x++;
												value=Integer.toHexString(x);
												if(value.length()==1 || value.length()==0) {
													value="000"+value;
												}
												else if(value.length()==2) {
													value="00"+value;
												}
												else if(value.length()==3) {
													value="0"+value;
												}
												registers[5]=value.substring(0, 2);
												registers[6]=value.substring(2,4);
												break;
									default:	return false;
										
								}
								
								
								
							}
							break;
				case "DCX": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String value="";
								int x;
								switch(line_Words[1]){
									case "B":
												value=registers[1]+registers[2];
												 x=Integer.parseInt(value,16);
												 x--;
												value=Integer.toHexString(x);
												if(value.length()==1 || value.length()==0) {
													value="000"+value;
												}
												else if(value.length()==2) {
													value="00"+value;
												}
												else if(value.length()==3) {
													value="0"+value;
												}
												registers[1]=value.substring(0, 2);
												registers[2]=value.substring(2,4);
												break;
									case "D":	
												value=registers[3]+registers[4];
												 x=Integer.parseInt(value,16);
												 x--;
												value=Integer.toHexString(x);
												if(value.length()==1 || value.length()==0) {
													value="000"+value;
												}
												else if(value.length()==2) {
													value="00"+value;
												}
												else if(value.length()==3) {
													value="0"+value;
												}
												registers[3]=value.substring(0, 2);
												registers[4]=value.substring(2,4);
												break;
									case "F": 
												value=registers[5]+registers[6];
												 x=Integer.parseInt(value,16);
												 x--;
												value=Integer.toHexString(x);
												if(value.length()==1 || value.length()==0) {
													value="000"+value;
												}
												else if(value.length()==2) {
													value="00"+value;
												}
												else if(value.length()==3) {
													value="0"+value;
												}
												registers[5]=value.substring(0, 2);
												registers[6]=value.substring(2,4);
												break;
									default:	return false;
										
								}
								
							}
							break;
				case "DAD": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								String value;
								
								switch(line_Words[1]) {
									case "B":	value=registers[1]+registers[2];	
												break;
									case "D":	value=registers[3]+registers[4];
												break;
									default:
												return false;
								}
								String value1=registers[5]+registers[6];
								int x=Integer.parseInt(value,16)+Integer.parseInt(value1,16);
								value1=Integer.toHexString(x);
								registers[6]=value1.substring(value1.length()-2);
								registers[5]=value1.substring(value1.length()-4, value1.length()-2);
							}
							break;
				case "SUI": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								if(line_Words[1].length()>2) {
									return false;
								}
								else {
									String f=registers[0].substring(registers[0].length()-1);
									String s=line_Words[1].substring(line_Words[1].length()-1);
									int accumulator=(int)Integer.parseInt(registers[0], 16);
									int data=(int)Integer.parseInt(line_Words[1],16);
									
									
									int sum=accumulator-data;
									String ans=Integer.toHexString(sum);
									if(ans.length()<=1) {
										registers[0]="0"+ans;
									}
									else {
										registers[0]=ans.substring(ans.length()-2);
									}
									
									int num=Integer.parseInt(registers[0],16);
									setFlag1(ans,f,s,num,flag,sum);
								}
								
							}
							break;
				case "CMA": 
							if(line_Words.length!=1) {
								return false;
							}
							registers[0]=compliment(registers[0]);
							break;
				case "CMP": 
							if(line_Words.length!=2) {
								return false;
							}
							else {
								int value;
								if(line_Words[1].equals("M")) {
									String m=registers[5]+registers[6];
									if(memory.containsKey(m)) {
										value=Integer.parseInt(memory.get(m),16);
									}
									else {
										return false;
									}
								}
								else if(line_Words[1].charAt(0)>='A' && line_Words[1].charAt(0)<='G') {
									value=Integer.parseInt(registers[line_Words[1].charAt(0)-65]);
								}
								else {
									return false;
								}
								int accumulator=Integer.parseInt(registers[0],16);
								if(accumulator<value) {
									flag[7]=true;
								}
								else if(accumulator==value) {
									flag[1]=true;
								}
								else {
									flag[7]=false;
									flag[1]=false;
								}
							}
							break;
				
				case "SET": 
							if(line_Words.length!=3) {
								return false;
							}
							else {
								if(line_Words[1].length()==4) {
									memory.put(line_Words[1], line_Words[2]);
								}
								else {
									System.out.println("Invalid Address");
								}
								
							}
							break;
				default: 
						return false;
			}
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static String compliment(String a) {
		int value=Integer.parseInt(a);
		a=Integer.toBinaryString(value);
		String q="";
		for(int i=0;i<(8-a.length());i++) {
			q=q+"0";
		}
		a=q+a;
		return a;
	}
	
	public static void setFlag(String ans,String f,String s,int num,boolean[] flag) {
		
		//carry flag
		if(ans.length()>2) {
			flag[7]=true;
		}
		else {
			flag[7]=false;
		}
		
		//Zero Flag
		
		if(num==0) {
			flag[1]=true;
		}
		else {
			flag[1]=false;
		}
		
		//Auxiliary Carry
		int v=Integer.parseInt(f,16)+Integer.parseInt(s,16);
		String v1=Integer.toHexString(v);
		if(v1.length()>1) {
			flag[3]=true;
		}
		else {
			flag[3]=false;
		}
		
		//Sign Flag
		if((num & Integer.parseInt("80",16)) ==Integer.parseInt("80",16)) {
			flag[0]=true;
		}
		else {
			flag[0]=false;
		}
		
		//parity flag
		int count=0;
		while(num>0) {
			if(!((num&1)==0)) {
				count++;
			}
			num=num/2;
		}
		if(count%2==0) {
			flag[5]=true;
		}
		else {
			flag[5]=false;
		}
		
		
	}
public static void setFlag1(String ans,String f,String s,int num,boolean[] flag,int sum1) {
		
		//carry flag
		if(sum1>=0) {
			flag[7]=false;
		}
		else {
			flag[7]=true;
		}
		
		//Zero Flag
		if(num==0) {
			flag[1]=true;
		}
		else {
			flag[1]=false;
		}
		
		//Auxiliary Carry
		if(Integer.parseInt(f,16)<Integer.parseInt(s,16)) {
			flag[3]=true;
		}
		else {
			flag[3]=false;
		}
		
		//Sign Flag
		if((num & Integer.parseInt("80",16)) ==Integer.parseInt("80",16)) {
			flag[0]=true;
		}
		else {
			flag[0]=false;
		}
		
		//parity flag
		int count=0;
		while(num>0) {
			if(!((num&1)==0)) {
				count++;
			}
			num=num/2;
		}
		if(count%2==0) {
			flag[5]=true;
		}
		else {
			flag[5]=false;
		}
		
		
	}

public int jumpStatements(String line,boolean[] flag,HashMap mainMemory) {
	
	String[] line_Words=line.split(" ");
	if(line_Words.length!=2) {
		return -1;
	}
	else {
		int a=Integer.parseInt(line_Words[1]);
		switch(line_Words[0]) {
			case "JMP": 
						if(mainMemory.containsKey(a)) {
								return a;
						}
						else {
							return -1;
						}	
						
			case "JC": 
						if(mainMemory.containsKey(a)) {
							if(flag[7]==true) {
								return a;
							}
							else {
								return 0;
							}
						}
						else {
							return-1;
						}
						
			case "JNC": 
						if(mainMemory.containsKey(a)) {
							if(flag[7]==false) {
								return a;
							}
							else {
								return 0;
							}
						}
						else {
							return-1;
						}
						
			case "JZ": 
						if(mainMemory.containsKey(a)) {
							if(flag[1]==true) {
								return a;
							}
							else {
								return 0;
							}
						}
						else {
							return-1;
						}
						
			case "JNZ": 
						if(mainMemory.containsKey(a)) {
							if(flag[1]==false) {
								return a;
							}
							else {
								return 0;
							}
						}
						else {
							return-1;
						}
			default: return -1;
						
		}
	}
	
}
}
