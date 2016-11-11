package com.koch.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.fasterxml.jackson.annotation.JsonFilter;

@Indexed
@Entity
@Table(name="article")
@JsonFilter("article")
public class Article extends BaseEntity{
	private static final long serialVersionUID = 9041677477874162977L;
	
	private static final int pageLength = 800;
	private static final String pageBreak = "<hr class=\"pageBreak\" />";
	private static final Pattern pattern = Pattern.compile("[,;\\.!?，；。！？]");
	
	private String author;
	private String articleTitle;
	private String content;
	private String htmlFilePath;
	private Boolean isPublish;
	private Boolean isRecommend;
	private Boolean isTop;
	private Integer hits;
	private String describtion;
	private String keywords;
	private String title;
	private Integer pageCount;
	private ArticleCategory articleCategory;
	
	private Set<Tag> tags = new HashSet<Tag>();
	
	@Field(store = Store.YES, index = Index.NO)
	@Length(max = 200)
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Lob
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column
	public String getHtmlFilePath() {
		return htmlFilePath;
	}
	public void setHtmlFilePath(String htmlFilePath) {
		this.htmlFilePath = htmlFilePath;
	}
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsTop() {
		return isTop;
	}
	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}
	@Column(nullable = false)
	public Integer getHits() {
		return hits;
	}
	public void setHits(Integer hits) {
		this.hits = hits;
	}
	@Column
	public String getDescribtion() {
		return describtion;
	}
	public void setDescribtion(String describtion) {
		this.describtion = describtion;
	}
	@Column
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	@Column
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="articleCategoryId",nullable = false)
	public ArticleCategory getArticleCategory() {
		return articleCategory;
	}
	public void setArticleCategory(ArticleCategory articleCategory) {
		this.articleCategory = articleCategory;
	}
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="article_tag", joinColumns={@JoinColumn(name="articleId")}, inverseJoinColumns={@JoinColumn(name="tagId")})  
	@OrderBy("orderList asc")
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	@Transient
	public String getText() {
		if (getContent() != null) {
			return Jsoup.parse(getContent()).text();
		}
		return null;
	}
	
	@Transient
	public String[] getPageContents() {
		if (StringUtils.isEmpty(this.getContent())) {
			return new String[] { "" };
		}
		if (this.getContent().contains(pageBreak)) {
			return this.getContent().split(pageBreak);
		}
		List<String> list = new ArrayList<String>();
		Document document = Jsoup.parse(this.getContent());
		List<Node> nodes = document.body().childNodes();
		if (nodes != null) {
			int i = 0;
			StringBuffer stringBuffer = new StringBuffer();
			Iterator<Node> iterator = nodes.iterator();
			while (iterator.hasNext()) {
				Node node = iterator.next();
				if ((node instanceof Element)) {
					Element element = (Element) node;
					stringBuffer.append(element.outerHtml());
					i += ((org.jsoup.nodes.Element) element).text().length();
					if (i >= pageLength) {
						list.add(stringBuffer.toString());
						i = 0;
						stringBuffer.setLength(0);
					}
				} else if (node instanceof TextNode) {
					TextNode textNode = (TextNode) node;
					String text = textNode.text();
					String[] arr = pattern.split(text);
					Matcher matcher = pattern.matcher(text);
					for (String str : arr) {
						if (matcher.find()) {
							str = str + matcher.group();
						}
						stringBuffer.append(str);
						i += str.length();
						if (i >= pageLength) {
							list.add(stringBuffer.toString());
							i = 0;
							stringBuffer.setLength(0);
						}
					}
				}
			}
			String rs = stringBuffer.toString();
			if (StringUtils.isNotEmpty(rs)) {
				list.add(rs);
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
	  
	@Transient
	public int getTotalPages() {
		return getPageContents().length;
	}
}
