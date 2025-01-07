package com.MDD_BACK.configuration;

import com.MDD_BACK.entity.*;
import com.MDD_BACK.repository.*;
import com.MDD_BACK.service.DataService;
import com.MDD_BACK.service.impl.RoleServiceImpl;
import com.MDD_BACK.service.impl.UtilisateurServiceImpl;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Configuration
public class DataInitializer {

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private UtilisateurServiceImpl utilisateurService;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbusername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private final UtilisateurRepository utilisateurRepository;
    private final ThemeRepository themeRepository;
    private final ArticleRepository articleRepository;
    private final CommentaireRepository commentaireRepository;
    private final RoleRepository roleRepository;
    private final DataService dataService;

    public DataInitializer(UtilisateurRepository utilisateurRepository, ThemeRepository themeRepository, ArticleRepository articleRepository, CommentaireRepository commentaireRepository, RoleRepository roleRepository, DataService dataService) {
        this.utilisateurRepository = utilisateurRepository;
        this.themeRepository = themeRepository;
        this.articleRepository = articleRepository;
        this.commentaireRepository = commentaireRepository;
        this.roleRepository = roleRepository;
        this.dataService = dataService;
    }

    @Bean
    @Transactional
    CommandLineRunner initDatabase() {
        return args -> {
            initSequences();
            initRoles();

            dataService.sauvegarderUtilisateurs(initUtilisateurs()).thenRun(() -> {
                dataService.obtenirUtilisateurs().thenAccept(persistedUtilisateurs -> {
                    assignRolesToUtilisateurs(persistedUtilisateurs);
                    List<Theme> themes = initThemes(persistedUtilisateurs);
                    dataService.sauvegarderThemes(themes).thenRun(() -> {
                        dataService.obtenirThemes().thenAccept(persistedThemes -> {
                            List<Article> articles = initArticles(persistedThemes, persistedUtilisateurs);
                            dataService.sauvegarderArticles(articles).thenRun(() -> {
                                List<Commentaire> commentaires = initCommentaires(persistedUtilisateurs, articles);
                                dataService.sauvegarderCommentaires(commentaires).thenRun(() -> {});
                            });
                        });
                    });
                });
            });
        };
    }

    private List<Utilisateur> initUtilisateurs() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<Utilisateur> utilisateurs = Arrays.asList(
                new Utilisateur("martin", "martin@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("jeanne", "jeanne@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("alice", "alice@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("john", "john@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("sarah", "sarah@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("pierre", "pierre@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("emilie", "emilie@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("lucas", "lucas@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("anna", "anna@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("kevin", "kevin@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("sophie", "sophie@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("julien", "julien@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("florence", "florence@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("yann", "yann@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("chloe", "chloe@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("antoine", "antoine@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("marie", "marie@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("maxime", "maxime@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("amelie", "amelie@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("thomas", "thomas@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin1", "admin1@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin2", "admin2@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin3", "admin3@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin4", "admin4@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin5", "admin5@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin6", "admin6@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin7", "admin7@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin8", "admin8@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin9", "admin9@example.com", passwordEncoder.encode("@&1234Azer")),
                new Utilisateur("admin10", "admin10@example.com", passwordEncoder.encode("@&1234Azer"))
        );
        return utilisateurs;
    }

  @Transactional
    public void assignRolesToUtilisateurs(List<Utilisateur> persistedUsers) {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found: ADMIN"));
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found: USER"));

        for (Utilisateur utilisateur : persistedUsers) {
            Hibernate.initialize(utilisateur.getRole());
            Set<Role> roles = new HashSet<>(utilisateur.getRole());

            boolean hasUserRole = utilisateurService.hasRole(utilisateur.getUsername(), "USER");
            if (!hasUserRole) {
                roles.add(userRole);
            }

            boolean hasAdminRole = utilisateurService.hasRole(utilisateur.getUsername(), "ADMIN");
            if (utilisateur.getUsername().toLowerCase().contains("admin") && !hasAdminRole) {
                roles.add(adminRole);
            }

            if (!roles.equals(utilisateur.getRole())) {
                utilisateur.setRole(roles);
                utilisateurRepository.save(utilisateur);
            }
        }
    }

    private void initSequences() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbusername, dbPassword);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS article_seq (next_val BIGINT)");
            stmt.execute("INSERT INTO article_seq (next_val) VALUES (1) ON DUPLICATE KEY UPDATE next_val = next_val");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initRoles() {
        List<Role> roles = Arrays.asList(new Role("USER"), new Role("ADMIN"));
        roles.forEach(role -> {
            if (!roleRepository.existsByName(role.getName())) {
                roleRepository.save(role);
            }
        });
    }


    private List<Theme> initThemes(List<Utilisateur> utilisateurs) {
        return Arrays.asList(
                new Theme("Apprentissage des langues", "Language Learning", utilisateurs.get(0), new HashSet<>(utilisateurs.subList(0, 10))),
                new Theme("Programmation en Java", "Java Programming", utilisateurs.get(1), new HashSet<>(utilisateurs.subList(10, 20))),
                new Theme("Développement Web", "Web Development", utilisateurs.get(2), new HashSet<>(utilisateurs.subList(0, 5))),
                new Theme("Base de données", "Database Management", utilisateurs.get(3), new HashSet<>(utilisateurs.subList(5, 10))),
                new Theme("Cybersécurité", "Cybersecurity", utilisateurs.get(4), new HashSet<>(utilisateurs.subList(10, 15))),
                new Theme("DevOps", "DevOps", utilisateurs.get(5), new HashSet<>(utilisateurs.subList(15, 20))),
                new Theme("Cloud Computing", "Cloud Computing", utilisateurs.get(6), new HashSet<>(utilisateurs.subList(0, 10))),
                new Theme("Intelligence Artificielle", "Artificial Intelligence", utilisateurs.get(7), new HashSet<>(utilisateurs.subList(10, 20))),
                new Theme("Machine Learning", "Machine Learning", utilisateurs.get(8), new HashSet<>(utilisateurs.subList(0, 5))),
                new Theme("Réseaux Informatiques", "Computer Networks", utilisateurs.get(9), new HashSet<>(utilisateurs.subList(5, 10))),
                new Theme("Big Data", "Big Data", utilisateurs.get(10), new HashSet<>(utilisateurs.subList(10, 15))),
                new Theme("Développement Mobile", "Mobile Development", utilisateurs.get(11), new HashSet<>(utilisateurs.subList(15, 20))),
                new Theme("Robotique", "Robotics", utilisateurs.get(12), new HashSet<>(utilisateurs.subList(0, 10))),
                new Theme("Blockchain", "Blockchain", utilisateurs.get(13), new HashSet<>(utilisateurs.subList(10, 20))),
                new Theme("Réalité Virtuelle", "Virtual Reality", utilisateurs.get(14), new HashSet<>(utilisateurs.subList(0, 5))),
                new Theme("IOT", "Internet of Things", utilisateurs.get(15), new HashSet<>(utilisateurs.subList(5, 10))),
                new Theme("SEO", "SEO", utilisateurs.get(16), new HashSet<>(utilisateurs.subList(10, 15))),
                new Theme("E-commerce", "E-commerce", utilisateurs.get(17), new HashSet<>(utilisateurs.subList(15, 20))),
                new Theme("Développement de Jeux Vidéo", "Game Development", utilisateurs.get(18), new HashSet<>(utilisateurs.subList(0, 10))),
                new Theme("Marketing Digital", "Digital Marketing", utilisateurs.get(19), new HashSet<>(utilisateurs.subList(10, 20)))
        );
    }

    private List<Commentaire> initCommentaires(List<Utilisateur> utilisateurs, List<Article> articles) {
        List<Commentaire> commentaires = new ArrayList<>();

        commentaires.add(new Commentaire("Super article ! Très informatif et bien écrit.", LocalDate.now(), utilisateurs.get(0), articles.get(0)));
        commentaires.add(new Commentaire("Merci pour ces informations précieuses et pertinentes.", LocalDate.now(), utilisateurs.get(1), articles.get(1)));

        commentaires.add(new Commentaire("Article bien structuré avec des points clairs.", LocalDate.now(), utilisateurs.get(2), articles.get(2)));
        commentaires.add(new Commentaire("Très bonne explication sur un sujet complexe.", LocalDate.now(), utilisateurs.get(3), articles.get(3)));

        commentaires.add(new Commentaire("Sujet intéressant et très bien traité.", LocalDate.now(), utilisateurs.get(4), articles.get(4)));
        commentaires.add(new Commentaire("Merci pour ce partage de connaissances.", LocalDate.now(), utilisateurs.get(5), articles.get(5)));

        commentaires.add(new Commentaire("Très utile, merci pour l'effort.", LocalDate.now(), utilisateurs.get(6), articles.get(6)));
        commentaires.add(new Commentaire("Bien expliqué, j'ai tout compris.", LocalDate.now(), utilisateurs.get(7), articles.get(7)));

        commentaires.add(new Commentaire("J'ai appris beaucoup de choses nouvelles.", LocalDate.now(), utilisateurs.get(8), articles.get(8)));
        commentaires.add(new Commentaire("Informations claires et bien présentées.", LocalDate.now(), utilisateurs.get(9), articles.get(9)));

        commentaires.add(new Commentaire("Contenu utile pour mieux comprendre.", LocalDate.now(), utilisateurs.get(10), articles.get(10)));
        commentaires.add(new Commentaire("Très bon article, merci pour le partage.", LocalDate.now(), utilisateurs.get(11), articles.get(11)));

        commentaires.add(new Commentaire("Article bien rédigé et informatif.", LocalDate.now(), utilisateurs.get(12), articles.get(12)));
        commentaires.add(new Commentaire("Merci pour ce contenu détaillé et précis.", LocalDate.now(), utilisateurs.get(13), articles.get(13)));

        commentaires.add(new Commentaire("Très informatif, merci pour les infos.", LocalDate.now(), utilisateurs.get(14), articles.get(14)));
        commentaires.add(new Commentaire("Article clair et précis sur le sujet.", LocalDate.now(), utilisateurs.get(0), articles.get(15)));

        commentaires.add(new Commentaire("Merci pour ces détails supplémentaires.", LocalDate.now(), utilisateurs.get(1), articles.get(16)));
        commentaires.add(new Commentaire("Explications concises et bien illustrées.", LocalDate.now(), utilisateurs.get(2), articles.get(17)));

        commentaires.add(new Commentaire("Très bonne lecture, j'ai beaucoup apprécié.", LocalDate.now(), utilisateurs.get(3), articles.get(18)));
        commentaires.add(new Commentaire("Très intéressant, j'ai appris beaucoup.", LocalDate.now(), utilisateurs.get(4), articles.get(19)));

        commentaires.add(new Commentaire("Article pertinent et bien documenté.", LocalDate.now(), utilisateurs.get(5), articles.get(20)));
        commentaires.add(new Commentaire("Informations utiles et bien expliquées.", LocalDate.now(), utilisateurs.get(6), articles.get(21)));

        commentaires.add(new Commentaire("Bon contenu, continuez comme ça.", LocalDate.now(), utilisateurs.get(7), articles.get(22)));
        commentaires.add(new Commentaire("Très bonne analyse, très instructif.", LocalDate.now(), utilisateurs.get(8), articles.get(23)));

        commentaires.add(new Commentaire("Merci pour cet article enrichissant.", LocalDate.now(), utilisateurs.get(9), articles.get(24)));

        return commentaires;
    }


private List<Article> initArticles(List<Theme> persistedThemes, List<Utilisateur> persistedUtilisateurs) {
    List<Article> articles = new ArrayList<>();

    Article article1 = new Article(
            persistedUtilisateurs.get(0),
            LocalDate.now(),
            "Cet article explore les différentes méthodes pour apprendre une nouvelle langue rapidement et efficacement, en utilisant des techniques modernes et des ressources en ligne.",
            "Méthodes efficaces pour apprendre une nouvelle langue"
    );
    article1.setTheme(persistedThemes.get(0));
    articles.add(article1);

    Article article2 = new Article(
            persistedUtilisateurs.get(1),
            LocalDate.now(),
            "Découvrez comment intégrer l'apprentissage des langues dans votre routine quotidienne grâce à des conseils pratiques et des astuces.",
            "Intégrer l'apprentissage des langues dans votre quotidien"

    );
    article2.setTheme(persistedThemes.get(0));
    articles.add(article2);

    Article article3 = new Article(
            persistedUtilisateurs.get(2),
            LocalDate.now(),
            "Cet article présente les meilleures applications mobiles pour apprendre une nouvelle langue de manière ludique et interactive.",
            "Applications mobiles pour apprendre les langues"

    );
    article3.setTheme(persistedThemes.get(0));
    articles.add(article3);

    Article article4 = new Article(
        persistedUtilisateurs.get(3),
        LocalDate.now(),
        "Découvrez les meilleures pratiques pour la programmation en Java, y compris les astuces pour optimiser votre code et éviter les erreurs courantes.",
            "Meilleures pratiques pour la programmation en Java"

);
article4.setTheme(persistedThemes.get(1));
        articles.add(article4);

Article article5 = new Article(
        persistedUtilisateurs.get(4),
        LocalDate.now(),
        "Cet article couvre les bases du langage Java, y compris les concepts fondamentaux et les structures de données essentielles.",
        "Bases du langage Java"

);
article5.setTheme(persistedThemes.get(1));
        articles.add(article5);

Article article6 = new Article(
        persistedUtilisateurs.get(5),
        LocalDate.now(),
        "Apprenez à créer des applications Java robustes en suivant ce guide détaillé, qui couvre les aspects de la conception, du développement et du déploiement.",
        "Créer des applications Java robustes"

);
article6.setTheme(persistedThemes.get(1));
        articles.add(article6);


        Article article7 = new Article(
        persistedUtilisateurs.get(6),
        LocalDate.now(),
        "Cet article couvre les bases du développement web, y compris les langages de programmation essentiels, les frameworks populaires et les outils de développement.",
                "Les bases du développement web"

);
article7.setTheme(persistedThemes.get(2));
        articles.add(article7);

Article article8 = new Article(
        persistedUtilisateurs.get(7),
        LocalDate.now(),
        "Découvrez comment créer des sites web réactifs et modernes en utilisant HTML, CSS et JavaScript, ainsi que des frameworks comme React et Angular.",
        "Créer des sites web réactifs et modernes"

);
article8.setTheme(persistedThemes.get(2));
        articles.add(article8);

Article article9 = new Article(
        persistedUtilisateurs.get(8),
        LocalDate.now(),
        "Apprenez à déployer et à héberger vos applications web en utilisant des plateformes cloud comme AWS, Azure et Google Cloud.",
        "Déployer et héberger des applications web"

);
article9.setTheme(persistedThemes.get(2));
        articles.add(article9);

Article article10 = new Article(
        persistedUtilisateurs.get(9),
        LocalDate.now(),
        "Explorez les concepts fondamentaux de la gestion des bases de données, y compris la modélisation des données, les requêtes SQL et les techniques d'optimisation.",
        "Introduction à la gestion des bases de données"

);
article10.setTheme(persistedThemes.get(3));
        articles.add(article10);

Article article11 = new Article(
        persistedUtilisateurs.get(10),
        LocalDate.now(),
        "Découvrez les meilleures pratiques pour la conception de bases de données relationnelles, ainsi que les différentes approches de normalisation.",
        "Conception de bases de données relationnelles"

);
article11.setTheme(persistedThemes.get(3));
        articles.add(article11);

Article article12 = new Article(
        persistedUtilisateurs.get(11),
        LocalDate.now(),
        "Apprenez à utiliser des bases de données NoSQL comme MongoDB et Cassandra, et comprenez les différences par rapport aux bases de données relationnelles.",
        "Bases de données NoSQL vs relationnelles"

);
article12.setTheme(persistedThemes.get(3));
        articles.add(article12);


Article article13 = new Article(
        persistedUtilisateurs.get(12),
        LocalDate.now(),
        "Apprenez les principes de base de la cybersécurité, y compris la protection des données personnelles, la détection des menaces et les bonnes pratiques pour assurer la sécurité en ligne.",
        "Principes de base de la cybersécurité"

);
article13.setTheme(persistedThemes.get(4));
        articles.add(article13);

Article article14 = new Article(
        persistedUtilisateurs.get(13),
        LocalDate.now(),
        "Découvrez les techniques avancées de protection contre les cyberattaques, ainsi que les outils et les stratégies pour renforcer la sécurité de votre réseau.",
        "Techniques avancées de protection contre les cyberattaques"

);
article14.setTheme(persistedThemes.get(4));
        articles.add(article14);

Article article15 = new Article(
        persistedUtilisateurs.get(14),
        LocalDate.now(),
        "Cet article explique comment mettre en place une politique de sécurité robuste pour votre organisation, y compris la gestion des accès et la formation des employés.",
        "Mise en place d'une politique de sécurité robuste"

);
article15.setTheme(persistedThemes.get(4));
        articles.add(article15);

Article article16 = new Article(
        persistedUtilisateurs.get(15),
        LocalDate.now(),
        "Découvrez les meilleures pratiques pour mettre en œuvre les pratiques DevOps dans votre organisation, afin de faciliter la collaboration entre les équipes de développement et d'exploitation.",
        "Meilleures pratiques DevOps"

);
article16.setTheme(persistedThemes.get(5));
        articles.add(article16);

Article article17 = new Article(
        persistedUtilisateurs.get(16),
        LocalDate.now(),
        "Cet article explique comment utiliser des outils DevOps comme Jenkins, Docker et Kubernetes pour automatiser et orchestrer les processus de déploiement.",
        "Outils DevOps pour l'automatisation"

);
article17.setTheme(persistedThemes.get(5));
        articles.add(article17);

Article article18 = new Article(
        persistedUtilisateurs.get(17),
        LocalDate.now(),
        "Apprenez comment améliorer la qualité et la rapidité des déploiements logiciels grâce à une intégration continue et une livraison continue (CI/CD) efficaces.",
        "Intégration continue et livraison continue (CI/CD)"

);
article18.setTheme(persistedThemes.get(5));
        articles.add(article18);


Article article19 = new Article(
        persistedUtilisateurs.get(18),
        LocalDate.now(),
        "Cet article explore les fondamentaux du Cloud Computing, y compris les principaux fournisseurs de services cloud et leurs offres.",
        "Introduction au Cloud Computing"

);
article19.setTheme(persistedThemes.get(6));
        articles.add(article19);

Article article20 = new Article(
        persistedUtilisateurs.get(19),
        LocalDate.now(),
        "Découvrez comment migrer vos applications et données vers le cloud en utilisant des stratégies éprouvées et des meilleures pratiques.",
        "Migration vers le cloud"

);
article20.setTheme(persistedThemes.get(6));
        articles.add(article20);

Article article21 = new Article(
        persistedUtilisateurs.get(20),
        LocalDate.now(),
        "Apprenez à gérer et optimiser vos ressources cloud pour améliorer les performances et réduire les coûts.",
        "Gestion et optimisation des ressources cloud"

);
article21.setTheme(persistedThemes.get(6));
        articles.add(article21);


Article article22 = new Article(
        persistedUtilisateurs.get(21),
        LocalDate.now(),
        "Explorez les concepts fondamentaux de l'intelligence artificielle, y compris les algorithmes de machine learning et les réseaux de neurones.",
        "Introduction à l'intelligence artificielle"

);
article22.setTheme(persistedThemes.get(7));
        articles.add(article22);

Article article23 = new Article(
        persistedUtilisateurs.get(22),
        LocalDate.now(),
        "Découvrez les applications pratiques de l'IA dans divers secteurs, tels que la santé, la finance et l'industrie.",
        "Applications pratiques de l'intelligence artificielle"

);
article23.setTheme(persistedThemes.get(7));
        articles.add(article23);

Article article24 = new Article(
        persistedUtilisateurs.get(23),
        LocalDate.now(),
        "Cet article explique les défis éthiques et les implications de l'intelligence artificielle, ainsi que les mesures pour garantir une IA responsable.",
        "Défis éthiques de l'intelligence artificielle"

);
article24.setTheme(persistedThemes.get(7));
        articles.add(article24);

Article article25 = new Article(
        persistedUtilisateurs.get(24),
        LocalDate.now(),
        "Cet article explore les concepts de base du machine learning, y compris les différents types d'algorithmes et leurs applications.",
        "Introduction au machine learning"

);
article25.setTheme(persistedThemes.get(8));
        articles.add(article25);

Article article26 = new Article(
        persistedUtilisateurs.get(25),
        LocalDate.now(),
        "Découvrez comment les modèles de machine learning sont entraînés et évalués, ainsi que les meilleures pratiques pour obtenir des performances optimales.",
        "Entraînement et évaluation des modèles de machine learning"

);
article26.setTheme(persistedThemes.get(8));
        articles.add(article26);

Article article27 = new Article(
        persistedUtilisateurs.get(26),
        LocalDate.now(),
        "Apprenez à implémenter des modèles de machine learning en utilisant des bibliothèques populaires telles que TensorFlow et scikit-learn.",
        "Implémentation des modèles de machine learning"

);
article27.setTheme(persistedThemes.get(8));
        articles.add(article27);


Article article28 = new Article(
        persistedUtilisateurs.get(27),
        LocalDate.now(),
        "Découvrez les principes de base des réseaux informatiques, y compris les différentes topologies de réseaux et les protocoles de communication.",
        "Principes de base des réseaux informatiques"

);
article28.setTheme(persistedThemes.get(9));
        articles.add(article28);

Article article29 = new Article(
        persistedUtilisateurs.get(28),
        LocalDate.now(),
        "Apprenez à configurer et gérer des réseaux locaux (LAN) et des réseaux étendus (WAN) en utilisant des équipements de réseau comme les routeurs et les commutateurs.",
        "Configuration et gestion des réseaux LAN et WAN"

);
article29.setTheme(persistedThemes.get(9));
        articles.add(article29);

Article article30 = new Article(
        persistedUtilisateurs.get(29),
        LocalDate.now(),
        "Cet article explore les techniques avancées de sécurité des réseaux, y compris la mise en œuvre de pare-feu, de VPN et de mesures de détection des intrusions.",
        "Sécurité avancée des réseaux"

);
article30.setTheme(persistedThemes.get(9));
        articles.add(article30);


Article article31 = new Article(
        persistedUtilisateurs.get(0),
        LocalDate.now(),
        "Cet article explore les concepts fondamentaux du Big Data, y compris la collecte, le stockage et l'analyse de grandes quantités de données.",
        "Introduction au Big Data"

);
article31.setTheme(persistedThemes.get(10));
        articles.add(article31);

Article article32 = new Article(
        persistedUtilisateurs.get(1),
        LocalDate.now(),
        "Découvrez les outils et les technologies utilisés dans le Big Data, comme Hadoop, Spark et les bases de données NoSQL.",
        "Outils et technologies du Big Data"

);
article32.setTheme(persistedThemes.get(10));
        articles.add(article32);

Article article33 = new Article(
        persistedUtilisateurs.get(2),
        LocalDate.now(),
        "Apprenez à extraire des informations précieuses des données en utilisant des techniques d'analyse avancées et des algorithmes de machine learning.",
        "Analyse avancée des données"

);
article33.setTheme(persistedThemes.get(10));
        articles.add(article33);


Article article34 = new Article(
        persistedUtilisateurs.get(3),
        LocalDate.now(),
        "Cet article explore les bases du développement mobile, y compris les langages de programmation, les frameworks et les outils utilisés pour créer des applications mobiles.",
        "Introduction au développement mobile"

);
article34.setTheme(persistedThemes.get(11));
        articles.add(article34);

Article article35 = new Article(
        persistedUtilisateurs.get(4),
        LocalDate.now(),
        "Découvrez comment développer des applications mobiles natives pour iOS et Android en utilisant Swift et Kotlin.",
        "Développement d'applications mobiles natives"

);
article35.setTheme(persistedThemes.get(11));
        articles.add(article35);

Article article36 = new Article(
        persistedUtilisateurs.get(5),
        LocalDate.now(),
        "Apprenez à créer des applications mobiles multiplateformes en utilisant des frameworks comme Flutter et React Native.",
        "Création d'applications mobiles multiplateformes"

);
article36.setTheme(persistedThemes.get(11));
        articles.add(article36);


Article article37 = new Article(
        persistedUtilisateurs.get(6),
        LocalDate.now(),
        "Découvrez les bases de la robotique, y compris les composants essentiels, les capteurs, et les actuateurs utilisés pour construire des robots.",
        "Introduction à la robotique"

);
article37.setTheme(persistedThemes.get(12));
        articles.add(article37);

Article article38 = new Article(
        persistedUtilisateurs.get(7),
        LocalDate.now(),
        "Cet article explore les applications de la robotique dans divers domaines, tels que l'industrie, la médecine et l'exploration spatiale.",
        "Applications de la robotique"

);
article38.setTheme(persistedThemes.get(12));
        articles.add(article38);

Article article39 = new Article(
        persistedUtilisateurs.get(8),
        LocalDate.now(),
        "Apprenez à programmer des robots en utilisant des langages de programmation populaires comme Python et C++.",
        "Programmation des robots"

);
article39.setTheme(persistedThemes.get(12));
        articles.add(article39);


Article article40 = new Article(
        persistedUtilisateurs.get(9),
        LocalDate.now(),
        "Explorez les concepts fondamentaux de la blockchain, y compris son fonctionnement, ses avantages et ses inconvénients.",
        "Introduction à la blockchain"

);
article40.setTheme(persistedThemes.get(13));
        articles.add(article40);

Article article41 = new Article(
        persistedUtilisateurs.get(10),
        LocalDate.now(),
        "Découvrez les différentes applications de la blockchain dans divers secteurs, tels que la finance, la logistique et la santé.",
        "Applications de la blockchain"

);
article41.setTheme(persistedThemes.get(13));
        articles.add(article41);

Article article42 = new Article(
        persistedUtilisateurs.get(11),
        LocalDate.now(),
        "Apprenez à développer des smart contracts en utilisant des plateformes comme Ethereum et Solidity.",
        "Développement de smart contracts"

);
article42.setTheme(persistedThemes.get(13));
        articles.add(article42);

Article article43 = new Article(
        persistedUtilisateurs.get(12),
        LocalDate.now(),
        "Découvrez les bases de la réalité virtuelle, y compris les technologies et les dispositifs utilisés pour créer des expériences immersives.",
        "Introduction à la réalité virtuelle"

);
article43.setTheme(persistedThemes.get(14));
        articles.add(article43);

Article article44 = new Article(
        persistedUtilisateurs.get(13),
        LocalDate.now(),
        "Cet article explore les différentes applications de la réalité virtuelle dans des domaines tels que les jeux, la médecine et l'éducation.",
        "Applications de la réalité virtuelle"

);
article44.setTheme(persistedThemes.get(14));
        articles.add(article44);

Article article45 = new Article(
        persistedUtilisateurs.get(14),
        LocalDate.now(),
        "Apprenez à développer des applications de réalité virtuelle en utilisant des plateformes et des outils populaires comme Unity et Unreal Engine.",
        "Développement d'applications de réalité virtuelle"

);
article45.setTheme(persistedThemes.get(14));
        articles.add(article45);


Article article46 = new Article(
        persistedUtilisateurs.get(15),
        LocalDate.now(),
        "Découvrez les concepts fondamentaux de l'Internet des objets (IoT), y compris les technologies et les protocoles utilisés pour connecter des appareils.",
        "Introduction à l'Internet des objets (IoT)"

);
article46.setTheme(persistedThemes.get(15));
        articles.add(article46);

Article article47 = new Article(
        persistedUtilisateurs.get(16),
        LocalDate.now(),
        "Cet article explore les différentes applications de l'IoT dans des secteurs tels que la maison intelligente, la santé et l'industrie.",
        "Applications de l'Internet des objets (IoT)"

);
article47.setTheme(persistedThemes.get(15));
        articles.add(article47);

Article article48 = new Article(
        persistedUtilisateurs.get(17),
        LocalDate.now(),
        "Apprenez à développer des projets IoT en utilisant des plateformes et des outils populaires comme Arduino et Raspberry Pi.",
        "Développement de projets IoT"

);
article48.setTheme(persistedThemes.get(15));
        articles.add(article48);


Article article49 = new Article(
        persistedUtilisateurs.get(18),
        LocalDate.now(),
        "Découvrez les bases du référencement naturel (SEO), y compris les techniques pour améliorer la visibilité de votre site web sur les moteurs de recherche.",
        "Introduction au référencement naturel (SEO)"

);
article49.setTheme(persistedThemes.get(16));
        articles.add(article49);

Article article50 = new Article(
        persistedUtilisateurs.get(19),
        LocalDate.now(),
        "Cet article explore les stratégies avancées de SEO, y compris l'optimisation des mots-clés, la création de contenu de qualité et les backlinks.",
        "Stratégies avancées de SEO"

);
article50.setTheme(persistedThemes.get(16));
        articles.add(article50);

Article article51 = new Article(
        persistedUtilisateurs.get(20),
        LocalDate.now(),
        "Apprenez à utiliser des outils de SEO pour analyser et améliorer les performances de votre site web, comme Google Analytics et SEMrush.",
        "Utilisation des outils de SEO"

);
article51.setTheme(persistedThemes.get(16));
        articles.add(article51);


Article article52 = new Article(
        persistedUtilisateurs.get(21),
        LocalDate.now(),
        "Découvrez les bases de l'e-commerce, y compris la création et la gestion d'une boutique en ligne et les différentes plateformes disponibles.",
        "Introduction à l'e-commerce"

);
article52.setTheme(persistedThemes.get(17));
        articles.add(article52);

Article article53 = new Article(
        persistedUtilisateurs.get(22),
        LocalDate.now(),
        "Cet article explore les stratégies pour attirer et fidéliser les clients en ligne, y compris le marketing digital et l'expérience utilisateur.",
        "Stratégies pour attirer et fidéliser les clients en ligne"

);
article53.setTheme(persistedThemes.get(17));
        articles.add(article53);

Article article54 = new Article(
        persistedUtilisateurs.get(23),
        LocalDate.now(),
        "Apprenez à optimiser votre site d'e-commerce pour améliorer les taux de conversion et augmenter vos ventes en ligne.",
        "Optimisation de site d'e-commerce"

);
article54.setTheme(persistedThemes.get(17));
        articles.add(article54);


Article article55 = new Article(
        persistedUtilisateurs.get(24),
        LocalDate.now(),
        "Découvrez les bases du développement de jeux vidéo, y compris les moteurs de jeu populaires, les langages de programmation et les étapes de conception.",
        "Introduction au développement de jeux vidéo"

);
article55.setTheme(persistedThemes.get(18));
        articles.add(article55);

Article article56 = new Article(
        persistedUtilisateurs.get(25),
        LocalDate.now(),
        "Cet article explore les différentes techniques pour créer des graphismes et des animations de qualité pour vos jeux vidéo.",
        "Création de graphismes et d'animations pour les jeux vidéo"

);
article56.setTheme(persistedThemes.get(18));
        articles.add(article56);

Article article57 = new Article(
        persistedUtilisateurs.get(26),
        LocalDate.now(),
        "Apprenez à optimiser les performances de vos jeux vidéo en utilisant des techniques de programmation avancées et des outils de profilage.",
        "Optimisation des performances des jeux vidéo"

);
article57.setTheme(persistedThemes.get(18));
        articles.add(article57);

Article article58 = new Article(
        persistedUtilisateurs.get(27),
        LocalDate.now(),
        "Découvrez les bases du design UX/UI, y compris les principes de conception centrée sur l'utilisateur et les meilleures pratiques pour créer des interfaces attrayantes.",
        "Introduction au design UX/UI"

);
article58.setTheme(persistedThemes.get(19));
        articles.add(article58);

Article article59 = new Article(
        persistedUtilisateurs.get(28),
        LocalDate.now(),
        "Cet article explore les différentes méthodes pour réaliser des recherches utilisateurs et comprendre les besoins et les attentes de votre public cible.",
        "Méthodes de recherche utilisateur"

);
article59.setTheme(persistedThemes.get(19));
        articles.add(article59);

Article article60 = new Article(
        persistedUtilisateurs.get(29),
        LocalDate.now(),
        "Apprenez à créer des prototypes interactifs en utilisant des outils de conception comme Sketch, Figma et Adobe XD.",
        "Création de prototypes interactifs"

);
article60.setTheme(persistedThemes.get(19));
        articles.add(article60);

return articles;
}

}









