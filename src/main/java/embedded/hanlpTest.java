package embedded;

import java.util.List;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;

public class hanlpTest {
	public static void main(String[] args) {
		String line = "公司拟将名称由山东联创节能新材料股份有限公司变更为山东联创互联网传媒股份有限公司";
		List<Term> termList = NLPTokenizer.segment(line);
		System.out.println(termList.toString());
	}
}
