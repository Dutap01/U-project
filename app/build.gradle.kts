plugins {
    // Gradle에게 필요한 플러그인을 로드하도록 지시합니다.
    java
    application
}

group = "com.example" 
version = "1.0-SNAPSHOT"

repositories {
    // 라이브러리를 중앙 Maven 저장소에서 가져옵니다.
    mavenCentral()
}

// =======================================================
// [중요] 프로젝트의 소스 경로 설정
// 모든 .java 파일이 'src' 폴더 안에 있기 때문에 필요합니다.
// =======================================================

dependencies {
    val javacvVersion = "1.5.10"
    val opencvVersion = "4.9.0"
    val tesseractVersion = "5.3.4"
    val junitVersion = "5.10.1"

    // 1. JavaCV 핵심 라이브러리
    implementation("org.bytedeco:javacv:$javacvVersion") 
    
    // 2. OpenCV 모듈 (영상 처리 및 카메라 접근에 필수)
    implementation("org.bytedeco:opencv:$opencvVersion-$javacvVersion") 
    
    // 3. Tesseract OCR (문자 인식에 필수)
    implementation("org.bytedeco:tesseract:$tesseractVersion-$javacvVersion") 
    
    // 4. 네이티브 종속성 (Windows 64비트 기준, 이 파일들이 카메라 접근에 필수)
    implementation("org.bytedeco:opencv:$opencvVersion-$javacvVersion:windows-x86_64")
    implementation("org.bytedeco:tesseract:$tesseractVersion-$javacvVersion:windows-x86_64") 
    testImplementation(platform("org.junit:junit-bom:$junitVersion")) 
    
    // 테스트 코드를 컴파일하는 데 필요한 API (Test, Assertions)
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    
    // 테스트 실행을 위한 엔진 (런타임에 필요)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
sourceSets {
    main {
        // 메인 소스 코드가 있는 경로를 정확히 지정합니다.
        // parking/src/U_project 에 .java 파일이 있으므로,
        // src/ 폴더 전체를 메인 소스 루트로 지정합니다.
        java.srcDirs("../src") // ⭐ parking 루트의 src 폴더를 참조
    }
    test {
        // 테스트 코드 경로도 명시적으로 지정하여 오류를 방지합니다.
        // 테스트 코드가 app/src/test/java 에 있다면 이 경로를 사용합니다.
        java.srcDirs("src/test/java") 
    }
}
application {
    mainClass.set("U_project.project")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    // sourceCompatibility 및 targetCompatibility는 toolchain 설정으로 대체됨
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}