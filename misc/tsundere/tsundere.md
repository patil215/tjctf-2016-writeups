# tsundere - 160 (Misc)
#### Writeup by Ehsan Asdar

## Problem ##
Just like a tsundere, this problem may seem difficult to deal with at first. But once you've had some time to get to know it, it'll show you its warmer side.

## Answer ##

### Overview ###
Reconstruct Java source code, realize `routine1` is md5, and decrypt the md5 hashes.

### Details ###
I reconstructed the Java source by hand, generating bytecode after each change and comparing it to the bytecode provided with the problem. I used this script to generate bytecode from my tsundere.java:
```bash
javac tsundere.java
javap -c tsundere.class
```

I reversed the main function completely, which ended up looking something like this.
```java
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
```

First, we check to make sure every character of the input is either a lowercase letter or a number. Then we split up the string into chunks of 5 and append the result of calling `routine1` on each chunk of 5 characters. Finally, we check this output with a hardcoded string to determine if our flag is right. At this point I set off on reversing `routine1`. The work I did to reverse it can be found [here](tsundere.java).

At the point where I stopped working on `routine1` (the method is incomplete) I decided to take a step back and think about what the function was doing. It used a lot of seemingly random and certainly destructive bit shifts, among other weird loops and calls (such as Math.sin() at one point). I checked if the length of the final output string was divisible by 32, and it was. That made `routine1` seem very suspicious. Guessing that `routine1` was an implementation of md5 called on 5 character blocks of the input string, I split the output string into blocks of 32 and decrypted each one using [hashkiller](https://hashkiller.co.uk/md5-decrypter.aspx). My hunch was correct, and we got the flag!

### Flag ###
  tjctf{j4hbjllsqyndjonfn6czn40f322p2ttgvbtg1m9pi33o8gmu368dkzpk7nldb8ztcdi3nb6ovjtawtovmmna2a67rjjoz6oun1if}
