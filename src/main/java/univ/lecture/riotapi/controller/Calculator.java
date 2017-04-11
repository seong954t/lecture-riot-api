package univ.lecture.riotapi.controller;

import java.util.Stack;
import java.util.StringTokenizer;

public class Calculator {
	public double calculate(String exp) {
		double data;
		if (exp.contains(")")) {
			int q = 0;
			Stack stack = new Stack<>();
			while (exp.length() != q) {
				StringBuilder calcul = new StringBuilder();
				char cal;
				while (exp.length() != q) {
					if((cal = exp.charAt(q++)) == ')'){
						break;
					}
					stack.push(cal);
				}
				while (!stack.empty()) {
					if((char) stack.peek() == '('){
						stack.pop();
						break;
					}
					calcul.insert(0, (char) stack.pop());
				}
				String v = Double.toString(calculate(calcul.toString()));
				int i = 0;
				while(v.length() != i){
					stack.push(v.charAt(i++));
				}
			}
			StringBuilder expdata = new StringBuilder();
			while (!stack.empty()) {
				expdata.insert(0, stack.pop());
			}
			exp = expdata.toString();
		}
		if (exp.contains("+")) {
			StringTokenizer st = new StringTokenizer(exp, "+");
			double cal = calculate(st.nextToken());
			while (st.hasMoreTokens()) {
				String name = st.nextToken();
				cal += calculate(name);
			}
			data = cal;
		} else if (exp.contains("-")) {
			StringTokenizer st = new StringTokenizer(exp, "-");
			String val = st.nextToken();
			char set = val.charAt(val.length() - 1);
			double cal;
			if (set == '*' || set == '/') {
				cal = calculate(st.nextToken());
				val += Double.toString(cal);
				cal = -calculate(val);
			} else {
				cal = calculate(val);
				if (!st.hasMoreTokens()) {
					cal = -cal;
				}
			}
			while (st.hasMoreTokens()) {
				String name = st.nextToken();
				cal -= calculate(name);
			}
			data = cal;
		} else if (exp.contains("*")) {
			StringTokenizer st = new StringTokenizer(exp, "*");
			double cal = calculate(st.nextToken());
			while (st.hasMoreTokens()) {
				String name = st.nextToken();
				cal *= calculate(name);
			}
			data = cal;
		} else if (exp.contains("/")) {
			StringTokenizer st = new StringTokenizer(exp, "/");
			double cal = calculate(st.nextToken());
			while (st.hasMoreTokens()) {
				String name = st.nextToken();
				cal /= calculate(name);
			}
			data = cal;
		} else {
			data = Double.parseDouble(exp);
		}
		return data;
	}
}
