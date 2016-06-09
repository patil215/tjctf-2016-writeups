public class tsundere {
   public static void main(String[] var0) {
      String var1 = "str";

      for(int var2 = 0; var2 < var1.length(); ++var2) {
         if((48 <= var1.charAt(var2) &&  57 >= var1.charAt(var2)) || (97 <= var1.charAt(var2) && 122 >= var1.charAt(var2)) ) {
           continue;
         }
         else{
           System.exit(9);
         }
      }
      String[] arr = var1.split("(?<=\\G.{5})");
      String something = "";
      for (int var4 = 0; var4 < arr.length; var4++){
        something += routine1(arr[var4].getBytes());
      }
      if (something.equals("C3704185F3162B815D577F12A54573E1907E9F272E076E837FAFB485BAB708B36205B07CCEE2104E5859C50D46379B8E4DDD05EC82EC41243A58314FE2EC19A5DA5CA17D002CA5AB179996F696C525A4D1158A0360EFAC7B6E13B747A98677E220B1D3CC33801999F0E34D3A466D9F1485E3C2779EDA3A4509B930D32C7BCE096517910DC8187CADA65E301E7A7C7838352D55CD0BA405536C1358D56552048CF4B8629B40C3BB8DDFF2D25554FF38F5580B72BE182F5D22D85CA678C5973C1746E31F03A6833828F5240549163903D8618B34779EE157B7B4C9B180B684CE9939FCBFEA91D068A5426CD432F0762A64519B3104854C46A2FE8A2D7057FA35ACCB69DC961943A11D9D7F2F11EDA8D1CBE6CF64BB437A7CE1BD3E01862BF2036E60BCEF1A1D3773A94FD05DB0A30A044C354FEDE02214D84F2F785A3BC2862495")){
        System.out.println("String Correct. Add \'tjctf{\' and \'}\' for the flag");
      }
   }
   public static String routine1(byte[] b){
     int one = (b.length + 8 >>> 6) + 1;
     int two = one << 6;
     byte[] arr = new byte[two-b.length];
     arr[0] = -128;
     long four = ((long)b.length)<< 3;
     int six = 0;

     while (six < 8){
       arr[arr.length-8+six] = (byte)(int)four;
       four = four >>> 8;
       six++;
     }
     six = 0;
     int secret7 = 0;
     int secret8 = 0;
     int secret9 = 0;

     int[] intarr = new int[16];
     intarr[0] = 7;
     intarr[1] = 12;
     intarr[2] = 17;
     intarr[3] = 22;
     intarr[4] = 5;
     intarr[5] = 9;
     intarr[6] = 14;
     intarr[7] = 20;
     intarr[8] = 4;
     intarr[9] = 11;
     intarr[10] = 16;
     intarr[11] = 23;
     intarr[12] = 6;
     intarr[13] = 10;
     intarr[14] = 15;
     intarr[15] = 21;

     int[] intarr2 = new int[64];
     for (int twelve = 0; twelve < 64; twelve++ ){
        intarr2[twelve] = (int)(long)(4.294967296E9d*(Math.abs(Math.sin( (double)(twelve+1)))));
     }

     int[] intarr3 = new int[16];
     for (int thirteen = 0; thirteen < one;thirteen++ ){
       int fourteen = thirteen << 6;
       for (int fifteen = 0; fifteen < 64;fifteen++){
         intarr3[fifteen >>> 2] =  ((fourteen < b.length ? b[fourteen] : arr[fourteen-b.length]) << 24) | ((intarr3[fifteen >>> 2]) >>>8) ;
         fourteen++;
       }
       int fifteen = six;
       int sixteen = secret7;
       int seventeen = secret8;
       int eighteen = secret9;
       for (int nineteen = 0; nineteen < 64; nineteen++){
         int twenty = nineteen >>> 4;
         int twentyone = 0;
         int twentytwo = nineteen;
         switch(twenty){
           case 0:
            twentyone = (secret7 & secret8) | ((secret7 ^ -1)&secret9);
            break;
           case 1:
            twentyone = (secret7 & secret9) | (secret8 & (secret9 ^ -1));
            twentytwo = (twentytwo*5 + 1) & 15;
            break;
           case 2:
            twentyone = (secret7 ^ secret8) ^ secret9;
            twentytwo = (twentytwo * 3 + 5) & 15;
            break;
           case 3:
            twentyone = ((secret9 ^ -1) | secret7) ^ secret8;
            twentytwo = (twentytwo * 7) & 15;
            break;
           default:
            break;
         }
       }

     }
     return null;
   }
}
