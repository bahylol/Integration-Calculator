import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static double Trapezoidal(double[] arr,double a,double b){
        double ans=0;
        double h= (b-a)/(arr.length-1);
        ans+=arr[0];
        ans+=arr[arr.length-1];
        for(int i=1;i< arr.length-1;i++){
            ans+=(arr[i]*2.0);
        }
        return (h/2.0)*ans;
    }
    public static double Simpson1_3(double[] arr,double a,double b){
        double ans=0;
        double h= (b-a)/(arr.length-1);
        ans+=arr[0];
        ans+=arr[arr.length-1];
        for(int i=1;i< arr.length-1;i++){
                if(i%2==0)
                    ans+=(arr[i]*2);
                else
                    ans+=(arr[i]*4);
        }
        return (h/3)*ans;
    }
    public static double Simpson1_8(double[] arr,double a,double b){
        double ans=0;
        double h= (b-a)/(arr.length-1);
        ans+=arr[0];
        ans+=arr[arr.length-1];
        for(int i=1;i< arr.length-1;i++){
            if(i%3==0)
                ans+=(arr[i]*2);
            else
                ans+=(arr[i]*3);
        }
        return ((3*h)/8)*ans;
    }
    public static void calc(String s,int N,double a,double b){
        double[] arr=new double[N+1];
        double[] trap=new double[2];
        double[] simps1_3=new double[3];
        double[] simps1_8=new double[4];
        double h=(b-a)/N;
        for(int i=0;i<=N;i++){
            arr[i]=evaluate(s,(a+i*h));
        }
        //basic Trapezoidal
        trap[0]=simps1_3[0]=simps1_8[0]=evaluate(s,a);
        trap[1]=simps1_3[2]=simps1_8[3]=evaluate(s,b);
        System.out.print("Basic Trapezoidal: ");
        System.out.println(Trapezoidal(trap,a,b));
        //composite Trapezoidal
        System.out.print("composite Trapezoidal: ");
        System.out.println(Trapezoidal(arr,a,b));
        //basic Simpson1_3
        simps1_3[1]=evaluate(s,(a+b)/2);
        System.out.print("basic Simpson1_3: ");
        System.out.println(Simpson1_3(simps1_3,a,b));
        //composite Simpson1_3
        if(N%2==0){
            System.out.print("composite Simpson1_3: ");
            System.out.println(Simpson1_3(arr,a,b));
        }
        //basic Simpson1_8
        simps1_8[1]=evaluate(s,(a+b)/3);
        simps1_8[2]=evaluate(s,((a+b)/3)*2);
        System.out.print("basic Simpson1_8: ");
        System.out.println(Simpson1_8(simps1_8,a,b));
        //composite Simpson1_8
        if(N%3==0){
            System.out.print("composite Simpson1_8: ");
            System.out.println(Simpson1_8(arr,a,b));
        }
    }

    public static double evaluate(String s,double x){
        boolean flag =false;
        if(s.charAt(0)=='-'){
            flag=true;
        }
        if(s.contains("cos")||s.contains("sin")||s.contains("ln")||s.contains("e")){
            s=convert(s,x);
        }
        if(flag)
            return -1* evaluatePostfix(postfix(s),x);
        return evaluatePostfix(postfix(s),x);
    }

    private static String convert(String s,double x){
        String ans="";
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='c'){
                i+=6;
                ans+="C";
            }
            else if(s.charAt(i)=='s'){
                i+=6;
                ans+='S';
            }
            else if(s.charAt(i)=='l'){
                i+=5;
                ans+='L';
            }
            else if(s.charAt(i)=='e'){
                i+=3;
                ans+='E';
            }
            if(i>=s.length())
                break;
            ans+=s.charAt(i);
        }
        return ans;
    }

    private static boolean priority(char a,char b){
        if(a=='^'||a=='√')
            return true;
        if((a=='*'||a=='/')&&(b=='+'||b=='-'))
            return true;
        return false;
    }

    private static String postfix(String x){
        String ans = "";
        Stack<Character> s = new Stack<Character>();
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) != '+' && x.charAt(i) != '-' && x.charAt(i) != '*' && x.charAt(i) != '/' && x.charAt(i) != '^' &&x.charAt(i) != '√' && x.charAt(i) != '(' && x.charAt(i) != ')')
                ans += "" + x.charAt(i);
            else if (x.charAt(i) == '(') {
                s.push(x.charAt(i));
            } else if (x.charAt(i) == ')') {
                ans += "" + s.pop();
                s.pop();
            } else {
                if (!s.empty()) {
                    if (priority(x.charAt(i), s.peek()) || x.charAt(i) == '(' || s.peek() == '(') {
                    }
                    else {
                        while (!s.empty() && !priority(x.charAt(i), s.peek())) {
                            ans += "" + s.pop();
                        }
                    }
                    s.push(x.charAt(i));
                } else s.push(x.charAt(i));
            }
        }
        while (!s.empty()) {
            ans += s.pop();
        }
        return ans;
    }

    static double evaluatePostfix(String exp,double x) {
        Stack<Double> stack=new Stack<>();
        for(int i=0;i<exp.length();i++)
        {
            char c=exp.charAt(i);
            if(Character.isDigit(c))
            {
                double n = 0;
                while(Character.isDigit(c))
                {
                    n = n*10 + (double) (c-'0');
                    i++;
                    if(i>=exp.length())
                        break;
                    c = exp.charAt(i);
                }
                double m=0.1;
                if(c=='.'){
                    do{
                        i++;
                        if(i>=exp.length())
                        break;
                        c = exp.charAt(i);
                        n+=((double)(c-'0')*m);
                        m*=0.1;
                    }
                    while(Character.isDigit(c));
                }
                i--;
                stack.push(n);
            }
            else if(c=='x')
                stack.push(x);
            else if(c=='E')
                stack.push(Math.exp(x));
            else if(c=='P')
                stack.push(Math.PI);
            else if(c=='C')
                stack.push(Math.cos(x));
            else if(c=='S')
                stack.push(Math.sin(x));
            else if(c=='L')
                stack.push(Math.log(x));
            else
            {
                double val1 = stack.pop();
                if(stack.isEmpty())
                    return val1;
                double val2 = stack.pop();

                switch(c)
                {
                    case '+':
                        stack.push(val2+val1);
                        break;

                    case '-':
                        stack.push(val2- val1);
                        break;

                    case '/':
                        stack.push(val2/val1);
                        break;

                    case '*':
                        stack.push(val2*val1);
                        break;

                    case '^':
                        stack.push(Math.pow(val2,val1));
                        break;
                    case '√':
                        stack.push(Math.pow(val1,1/val2));
                        break;
                }
            }
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter expression : ");
        String equation= sc.next();
        System.out.println("Enter value for N : ");
        int N= sc.nextInt();
        System.out.println("Enter a : ");
        String a= sc.next();
        System.out.println("Enter b : ");
        String b= sc.next();
        calc(equation,N,evaluatePostfix(postfix(a),1),evaluatePostfix(postfix(b),1));
        //pie=P sqrt=alt 2 5 1
    }
}
