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
