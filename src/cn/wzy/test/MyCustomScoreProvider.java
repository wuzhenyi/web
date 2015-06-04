package cn.wzy.test;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;

public class MyCustomScoreProvider extends CustomScoreProvider {

	private AtomicReaderContext context;
	
	public MyCustomScoreProvider(AtomicReaderContext context) {
		super(context);
		this.context = context;
	}
	
	@Override
	public float customScore(int doc, float subQueryScore, float valSrcScore)
			throws IOException {
//		Document d = context.reader().document(doc);
//		if (d.get("title").toLowerCase().contains("iphone")) {
//			return subQueryScore * valSrcScore * 2;
//		}
		return subQueryScore * valSrcScore;
	}

}
