package com.anonyblah.xxalimi.controls;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.anonyblah.xxalimi.dao.ArticlesDao;
import com.anonyblah.xxalimi.dao.FeedsDao;
import com.anonyblah.xxalimi.service.ArticleService;
import com.anonyblah.xxalimi.service.FeedService;
import com.anonyblah.xxalimi.service.KeywordService;
import com.anonyblah.xxalimi.service.LoginService;
import com.anonyblah.xxalimi.vo.Articles;
import com.anonyblah.xxalimi.vo.Feeds;

/**
 * Feed 관리 Controller
 * 
 * @see FeedsDao
 * @see ArticlesDao
 */
@Controller
@RequestMapping("/user")
public class HomeController {

	
	@Autowired
	LoginService loginService;

	@Autowired
	FeedService feedService;
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	KeywordService keywordService;
	
	/**
	 * @deprecated
	 */

	/**
	 * 대문용
	 * 
	 * @return 표시할 View의 이름
	 */

	/**
	 * Feed목록을 불러와 JSPContext에 담아와 home View에서 FeedList를 사용
	 * 
	 * @param model
	 *            JSPContext
	 * @return 표시할 View의 이름
	 * @throws Exception
	 */
	@RequestMapping("/home") // "/home"으로 요청이 들어왔을때 이 Method 호출
	public String home(HttpServletRequest request, Model model) throws Exception {
		

		String email = request.getUserPrincipal().getName();
		loginService.saveID(email);
		List<Feeds> feedList = feedService.outputFeed();
		List<Articles> articleList = articleService.outputArticles();
		
		model.addAttribute("feedList", feedList);
		model.addAttribute("articleList", articleList);

		return "/user/home";
	}

	@RequestMapping(value = "/delete/feed/{feedtitle}", method = RequestMethod.DELETE)
	public String deleteFeeds(Model model,@PathVariable String feedtitle, @RequestParam String feedUrl) throws Exception {
		
		feedService.deleteFeed(feedUrl);
		keywordService.deleteKeyword(feedUrl);
		
		return "redirect:/user/home";
	}
	
	/**
	 * MindMapUI (Experimental)
	 * 
	 * @param model
	 *            JSPContext
	 * @return 표시할 View의 이름
	 * @throws Exception 
	 */
	@RequestMapping("/home/mindmap")
	public String mindmap(Model model) throws Exception {

		List<Feeds> feedList = feedService.outputFeed();
		List<Articles> articleList = articleService.outputArticles();
		
		
		model.addAttribute("feedList", feedList);
		model.addAttribute("articleList", articleList);

		return "/user/mindmap";
	}

	/**
	 * Feed를 누르면 그 피드의 Article들을 보여주기 위한 View Method
	 * 
	 * @param model
	 *            JSPContext
	 * @param title
	 *            Feed의 이름
	 * @return 표시할 View의 이름
	 * @throws Exception
	 */
	@RequestMapping("/home/feed/{feedtitle}")
	// {title}은 @PathVariable라는 Annotation이 붙은 매개변수 title을 사용
	public String viewFeed(HttpServletRequest request, Model model, @PathVariable String feedtitle) throws Exception {

		List<Articles> articles = articleService.outputArticlesByTitle(feedtitle);

		model.addAttribute("title", articles.get(0).getFeedTitle());
		if (articles.get(0).getPublishedDate() != null)
			model.addAttribute("pubDate", articles.get(0).getPublishedDate());

		model.addAttribute("articleList", articles);

		return "/user/feedView";
	}

	// @RequestMapping("/home/article/{title}")
	// // {title}은 @PathVariable라는 Annotation이 붙은 매개변수 title을 사용
	// public String viewArticle(Model model, @PathVariable String title) {
	// List<SyndFeed> list = FeedList.feedList;
	// SyndFeed feed = null;
	// SyndEntry article = null;
	// for (int i = 0; i < list.size(); i++) {
	// feed = list.get(i);
	// @SuppressWarnings("unchecked")
	// List<SyndEntry> articleList = feed.getEntries();
	// for (int j = 0; j < articleList.size(); j++) {
	// if (articleList.get(j).getTitle().equals(title)) {
	// article = articleList.get(j);
	// }
	// }
	//
	// }
	// model.addAttribute("title", article.getTitle());
	// model.addAttribute("pubDate", article.getPublishedDate());
	// model.addAttribute("article", article);
	//
	// return "/user/articleView";
	// }

	/**
	 * FeedFresh라는 버튼을 누르면 정해진 URI에서 Feed를 읽어 저장하고 다시 Home으로 Redirect
	 * 
	 * @return 표시할 View의 이름
	 */
	@RequestMapping("/home/refreshFeed")
	public String refreshFeed() {

		return "redirect:/user/home";
	}

}
