package net.javaguides.springboot.springsecurity.web;

import net.javaguides.springboot.springsecurity.model.Article;
import net.javaguides.springboot.springsecurity.model.Photos;
import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.repository.ArticleRepository;
import net.javaguides.springboot.springsecurity.repository.PhotosRepository;
import net.javaguides.springboot.springsecurity.repository.UserRepository;
import net.javaguides.springboot.springsecurity.service.ArticleService;
import net.javaguides.springboot.springsecurity.service.PhotosService;
import net.javaguides.springboot.springsecurity.service.UserService;
import net.javaguides.springboot.springsecurity.web.dto.ArticleRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {

    private final ArticleRepository articleRepository;
    private UserRepository userRepository;
    private PhotosRepository photosRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PhotosService photosService;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, UserRepository userRepository, PhotosRepository photosRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.photosRepository = photosRepository;
    }


    @ModelAttribute("article")
    public ArticleRegistrationDto articleRegistrationDto() {
        return new ArticleRegistrationDto();
    }

    @GetMapping("/add")
    public String showAddArticleForm(Model model) {
        return "/addArticle";
    }

    @PostMapping("/add")
    public String registerArticle(@ModelAttribute("article") @Valid ArticleRegistrationDto articleDto,
                                  BindingResult result, @RequestParam("files") MultipartFile[] files) throws IOException {

        String message = "";
        var lambdaContext = new Object() {
            Boolean first = true;
        };

        articleDto.setPhoto(files[0].getOriginalFilename());
        Article savedArticle = articleService.save(articleDto);
        String uploadDir = "article-photos/" + savedArticle.getArticleId();

        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.asList(files).stream().forEach(file -> {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                if (fileNames.size() < 10) {
                    try {
                        photosService.save(savedArticle, fileName);
                        FileUploadUtil.saveFile(uploadDir, fileName, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileNames.add(file.getOriginalFilename());
                }
            });
        } catch (Exception e) {
            message = "Nie udało się wysłać plików.";
        }

        if (result.hasErrors()) {
            return "/addArticle";
        }

        return "redirect:/article/list";
    }

    @RequestMapping("/list")
    public String showFirtsPage(Model model,
                                @Param("keyword") String keyword,
                                @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        return showUpdateForm(model, keyword, searchKeyword, 1);
    }

    @RequestMapping("/list/{pageNum}")
    public String showUpdateForm(Model model,
                                 @Param("keyword") String keyword,
                                 @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                                 @PathVariable(name = "pageNum") int pageNum) {
        if(pageNum == 0 )
            pageNum = 1 ;

        Page<Article> page = articleService.listAll(keyword, searchKeyword, pageNum);
        List<Article> listProducts = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("articles", listProducts);
        model.addAttribute("keyword", keyword);

        return "listArticle";
    }


    @GetMapping("/{id}")
    public String showArticle(@PathVariable("id") long id, Model model) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));

        User user = userRepository.findById(article.getUser_owner_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        ;
        model.addAttribute("Photos", article.getPhotos());
        model.addAttribute(article);
        model.addAttribute(user);
        return "showArticle";
    }

    @RequestMapping("/listMy")
    public String showMyArticles(Model model,
                                 @Param("keyword") String keyword,
                                 @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        return changeAvailability(model, keyword, searchKeyword, 1);
    }

    @RequestMapping("/listMy/{pageNum}")
    public String changeAvailability(Model model,
                @Param("keyword") String keyword,
                @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                @PathVariable(name = "pageNum") int pageNum) {

        if(pageNum == 0 ) pageNum = 1 ;
        Long MyUserId = userService.loadCurrentUser().getUserId();
        Page<Article> pageMy = articleService.listAllMy(keyword, searchKeyword, pageNum,  MyUserId);
        List<Article> listProducts = pageMy.getContent();

        model.addAttribute("myArticles", listProducts);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageMy.getTotalPages());
        model.addAttribute("totalItems", pageMy.getTotalElements());
        model.addAttribute("keyword", keyword);


        return "listMyArticle";
    }

    @GetMapping("/listAviabiltyMy/{id}")
    public String changeAvi(@PathVariable("id") long id, Model model) {
        User currUser = userService.loadCurrentUser();
        model.addAttribute("myArticles", currUser.getArticles());

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));

        article.setAvailable(!article.getAvailable());
        articleService.updateArticle(article);
        return "redirect:/article/listMy";
    }


    @GetMapping("/editArticle/{id}")
    public String showEditArticleForm(@PathVariable("id") long id, Model model) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
        model.addAttribute("article", article);
        return "editArticle";
    }

    @PostMapping("/updateArticle/{id}")
    public String editArticlePost(@PathVariable("id") long id, @ModelAttribute("article") @Valid Article article, Model model) {
        articleService.updateArticle(article);
        User currUser = userService.loadCurrentUser();
        model.addAttribute("myArticles", currUser.getArticles());
        return "listMyArticle";
    }

    @GetMapping("deleteArticle/{id}")
    public String deleteArticle(@PathVariable("id") long id, Model model) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
        articleService.delete(article);
        User currUser = userService.loadCurrentUser();
        model.addAttribute("myArticles", currUser.getArticles());
        return "listMyArticle";
    }

//    @GetMapping("/deleteArticle/{id}")
//    public String deleteArt(@PathVariable(name = "id") int id) {
//        long l = id;
//        articleService.deleteArticle(l);
//        return "redirect:/article/listMy";
//    }



}
