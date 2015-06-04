package cn.wzy.search.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import cn.wzy.jcseg.analyzer.JcsegAnalyzer4X;
import cn.wzy.jcseg.core.DictionaryFactory;
import cn.wzy.jcseg.core.ISegment;
import cn.wzy.jcseg.core.IWord;
import cn.wzy.jcseg.core.JcsegTaskConfig;
import cn.wzy.jcseg.core.SegmentFactory;
import cn.wzy.search.enums.ArticleTypeEnum;
import cn.wzy.search.pojo.Article;

public class JcsegUtil {
	
	private static Directory dir = null;
	private static Directory taxodir = null;
	private static DirectoryReader reader = null;
	private static FacetsConfig fconfig = null;
	
	static {
		try {
			dir = FSDirectory.open(new File(PropertiesUtil.get("indexdir")));
			taxodir = FSDirectory.open(new File(PropertiesUtil.get("taxodir")));
			fconfig = new FacetsConfig();
//			fconfig.setHierarchical("Publish Date", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static IndexSearcher getIndexSearcher(Directory indexDir) {

		IndexSearcher searcher = null;

		try {
			reader = DirectoryReader.open(indexDir);
			DirectoryReader dr = DirectoryReader.openIfChanged(reader);
			if (dr != null) {
				reader.close();
				reader = dr;
			}
			searcher = new IndexSearcher(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return searcher;
	}
	
	public static JcsegAnalyzer4X getAnalyzer() {
		JcsegAnalyzer4X jcseg = new JcsegAnalyzer4X(JcsegTaskConfig.COMPLEX_MODE);
		JcsegTaskConfig config = jcseg.getTaskConfig();
		config.setLoadCJKPinyin(true);
		config.setLoadCJKSyn(true);
		config.setAppendCJKPinyin(true);
		config.setAppendCJKSyn(true);
		config.setAppendCJKJianpin(true);
		return jcseg;
	}
	
	public static void index(List<Article> list, boolean isNew) {
		IndexWriter writer = null;
		try {
			JcsegAnalyzer4X jcseg = getAnalyzer();
			writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_4_10_2, jcseg));
			if(isNew)
				writer.deleteAll();
			Document doc = null;
			for (Article article : list) {
				doc = new Document();
				doc.add(new IntField("id", article.getId(), Store.YES));
				TextField field = new TextField("title", article.getTitle(), Store.YES);
//				if (article.getTitle().toLowerCase().contains("中国"))
//					field.setBoost(20.0f);
//				else
//					field.setBoost(0.5f);
//				if ("互联网".equals(getTypeName(article.getType())))
//					field.setBoost(20.0f);
//				else
//					field.setBoost(0.5f);
				doc.add(field);
				doc.add(new LongField("createtime", article.getCreateTime().getTime(), Store.YES));
				doc.add(new TextField("content", filterHtml(article.getContent()), Store.NO));
				writer.addDocument(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer!=null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static void facetIndex(List<Article> list, boolean isNew) {
		IndexWriter writer = null;
		DirectoryTaxonomyWriter dtw = null;
		try {
			JcsegAnalyzer4X jcseg = getAnalyzer();
			writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_4_10_2, jcseg));
			dtw = new DirectoryTaxonomyWriter(taxodir, OpenMode.CREATE);
			if(isNew)
				writer.deleteAll();
			Document doc = null;
			for (Article article : list) {
				doc = new Document();
				doc.add(new IntField("id", article.getId(), Store.YES));
				TextField field = new TextField("title", article.getTitle(), Store.YES);
				// if ("互联网".equals(getTypeName(article.getType())))//类别是互联网的优先
				// field.setBoost(20.0f);
				// else
				// field.setBoost(0.5f);
				// if
				// (article.getTitle().toLowerCase().contains("美国"))//标题中有美国的优先
				// field.setBoost(20.0f);
				// else
				// field.setBoost(0.5f);
				doc.add(field);
				doc.add(new LongField("createTime", article.getCreateTime().getTime(), Store.YES));
				doc.add(new LongField("modifyTime", article.getModifyTime().getTime(), Store.YES));
				doc.add(new TextField("content", filterHtml(article.getContent()), Store.NO));
				doc.add(new IntField("isShow", booleanToInt(article.getIsShow()), Store.YES));
				doc.add(new StringField("addUser", article.getAddUser(), Store.YES));
				doc.add(new StringField("modifyUser", article.getModifyUser(), Store.YES));
				doc.add(new IntField("type", article.getType(), Store.YES));
				FacetField ff = new FacetField("type", getTypeName(article.getType()));
				// if ("互联网".equals(getTypeName(article.getType())))
				// ff.setBoost(10.0f);
				// else
				// ff.setBoost(0.5f);
				doc.add(ff);
				
				// if (article.getTitle().toLowerCase().contains("iphone"))
				// doc.add(new FloatField("score", 0.1f, Store.NO));
				// else
				// doc.add(new FloatField("score", 1.0f, Store.NO));
				writer.addDocument(fconfig.build(dtw, doc));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer!=null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(dtw!=null)
				try {
					dtw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static int booleanToInt(Boolean isShow) {
		if (isShow)
			return 1;
		return 0;
	}
	
	public static String getTypeName(Integer type) {
		if (type == ArticleTypeEnum.Network.getValue())
			return ArticleTypeEnum.Network.getName();
		else if (type == ArticleTypeEnum.Economy.getValue())
			return ArticleTypeEnum.Economy.getName();
		else
			return ArticleTypeEnum.YangSheng.getName();
	}

	public static List<Document> search(String[] fields, Set<String> wds) {
		List<Document> docs = new ArrayList<Document>();
		IndexSearcher searcher = getIndexSearcher(dir);
		BooleanQuery bq = new BooleanQuery();
		BooleanQuery titleQ = new BooleanQuery();
		BooleanQuery contentQ = new BooleanQuery();
		for (int i = 0; i < fields.length; i++) {
			for (String wd : wds) {
				TermQuery query = new TermQuery(new Term(fields[i], wd));
//				query = setBoost(query, fields[i]);
				if (fields[i].contains("title")) {
					titleQ.add(query, Occur.SHOULD);
				}else {
					contentQ.add(query, Occur.SHOULD);
				}
			}
		}
		bq.add(titleQ, Occur.SHOULD);
		bq.add(contentQ, Occur.SHOULD);
//		MyCustomScoreQuery mq = new MyCustomScoreQuery(bq);
		try {
//			TopDocs td = searcher.search(bq, 10,new Sort(new SortField("score", Type.FLOAT)));
			TopDocs td = searcher.search(bq, 10);
			if (td != null && td.totalHits > 0) {
				for (ScoreDoc sd : td.scoreDocs) {
					Document doc = searcher.doc(sd.doc);
					System.out.println("Score--->" + sd.score + " ID-->" + sd.doc + " Title--->" + doc.get("title") + 
							" CreateTime--->" + doc.get("createtime"));
					docs.add(doc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return docs;
	}
	
	private static TermQuery setBoost(TermQuery query, String field) {
		switch (field) {
		case "title":
			query.setBoost(3.0f);
			break;
		case "content":
			query.setBoost(2.0f);
			break;
		default:
			query.setBoost(1.0f);
			break;
		}
		return query;
	}

	public static String filterHtml(String src) {   
        Pattern pattern = Pattern.compile("<([^>]*)>");   
        Matcher matcher = pattern.matcher(src);   
        StringBuffer sb = new StringBuffer();   
        boolean flag = matcher.find();   
        while (flag) {   
            matcher.appendReplacement(sb, "");   
            flag = matcher.find();   
        }
        matcher.appendTail(sb);   
        return sb.toString();   
    }
	
	public static Set<String> getWds(String wd) {
		Set<String> wds = new HashSet<String>();
		try {
			JcsegTaskConfig cfg = new JcsegTaskConfig();
			cfg.setLoadCJKSyn(true);
			cfg.setLoadCJKPinyin(true);
			cfg.setAppendCJKPinyin(true);
			cfg.setAppendCJKSyn(true);
			cfg.setAppendCJKJianpin(true);
			ISegment seg = SegmentFactory.createJcseg(
					JcsegTaskConfig.COMPLEX_MODE,
					new Object[]{cfg, DictionaryFactory.createDefaultDictionary(cfg)});
			StringReader sr = new StringReader(wd);
			seg.reset(sr);
			IWord iw = null;
			while ((iw = seg.next()) != null) {
				if (!StringUtil.isNullOrBlank(iw.getPinyin()))
					wds.add(iw.getPinyin());
				if (iw.getSyn() != null && iw.getSyn().length > 0) {
					for (String w : iw.getSyn())
						wds.add(w);
				}
				wds.add(iw.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wds;
	}
	
	public static List<Document> facetSearch(String[] fields, Set<String> wds) {
		List<Document> docs = new ArrayList<Document>();
		IndexSearcher searcher = getIndexSearcher(dir);
		DirectoryTaxonomyReader tr = null;
		try {
			tr = new DirectoryTaxonomyReader(taxodir);
			FacetsCollector fc = new FacetsCollector();
			BooleanQuery bq = new BooleanQuery();
			BooleanQuery titleQ = new BooleanQuery();
			BooleanQuery contentQ = new BooleanQuery();
			for (int i = 0; i < fields.length; i++) {
				for (String wd : wds) {
					TermQuery query = new TermQuery(new Term(fields[i], wd));
//					query = setBoost(query, fields[i]);
					if (fields[i].contains("title")) {
						titleQ.add(query, Occur.SHOULD);
					}else if (fields[i].contains("content")) {
						contentQ.add(query, Occur.SHOULD);
					}
				}
			}
//			Query q = MultiFieldQueryParser.parse(queries, fields, getAnalyzer());
			bq.add(titleQ, Occur.SHOULD);
			bq.add(contentQ, Occur.SHOULD);
//			TopDocs td = searcher.search(bq, 10);
//			TopDocs td = FacetsCollector.search(searcher, bq, 10, fc);
//			Facets facets = new FastTaxonomyFacetCounts(tr, fconfig, fc);
			
			//只搜结果为经济类型的类别
			DrillDownQuery drillDownQuery = new DrillDownQuery(fconfig, bq);
//	        drillDownQuery.add("type", "互联网");
//	        TopDocs td = FacetsCollector.search(searcher, drillDownQuery, null, 10, 
//	        		new Sort(new SortField("createtime", Type.LONG, true)),fc);
			TopDocs td = FacetsCollector.search(searcher, drillDownQuery, 10, fc);
	        Facets facets = new FastTaxonomyFacetCounts(tr, fconfig, fc); 
	        List<FacetResult> results = facets.getAllDims(10);
	        for (FacetResult tmp : results) {
	            System.out.println(tmp);
	        }
	        if (td != null && td.totalHits > 0) {
				for (ScoreDoc sd : td.scoreDocs) {
					Document doc = searcher.doc(sd.doc);
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(Long.parseLong(doc.get("createTime")));
					System.out.println(
							"Score--->" + sd.score
							+ " ID-->" + sd.doc 
							+ " Title--->" + doc.get("title") 
							+ " CreateTime--->" + c.getTime());
					docs.add(doc);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tr != null)
				try {
					tr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return docs;
	}
	
	public static void main(String[] args) throws Exception {
//		search(new String[]{"title", "content"},  JcsegUtil.getWds("iphone"));
		facetSearch(new String[]{"title", "content"},  JcsegUtil.getWds("zg"));
	}
}
