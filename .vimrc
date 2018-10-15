"" Last update: 15.10.2018 23:45
set nocompatible              " be iMproved, required
filetype off                  " required

" set the runtime path to include Vundle and initialize
set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()
" alternatively, pass a path where Vundle should install plugins
"call vundle#begin('~/some/path/here')

" let Vundle manage Vundle, required
Plugin 'VundleVim/Vundle.vim'
" Plugin 'vim-scripts/LineJuggler'
Plugin 'svermeulen/vim-easyclip'
Plugin 'tpope/vim-repeat'
Plugin 'yegappan/grep'
Plugin 'scrooloose/nerdcommenter'
Plugin 'flazz/vim-colorschemes'
" Plugin 'vim-syntastic/syntastic'
Plugin 'w0rp/ale'
Plugin 'SirVer/ultisnips'
Plugin 'jiangmiao/auto-pairs'
" if exists('NOTES')
" else
  Plugin 'Valloric/YouCompleteMe'
  Plugin 'Yggdroot/indentLine'
  Plugin 'Lokaltog/vim-powerline'
" endif
Plugin 'junegunn/vim-easy-align'
Plugin 'jlanzarotta/bufexplorer'
Plugin 'scrooloose/nerdtree'
Plugin 'Xuyuanp/nerdtree-git-plugin'
Plugin 'tpope/vim-fugitive'
Plugin 'tpope/vim-surround'
Plugin 'sukima/xmledit'
Plugin 'vim-scripts/Conque-GDB'
Plugin 'terryma/vim-multiple-cursors'
Plugin 'xolox/vim-misc'
Plugin 'xolox/vim-session'

" All of your Plugins must be added before the following line
call vundle#end()            " required
"filetype plugin indent on    " required
" To ignore plugin indent changes, instead use:
filetype plugin on
"
" Brief help
" :PluginList       - lists configured plugins
" :PluginInstall    - installs plugins; append `!` to update or just :PluginUpdate
" :PluginSearch foo - searches for foo; append `!` to refresh local cache
" :PluginClean      - confirms removal of unused plugins; append `!` to auto-approve removal
"
" see :h vundle for ore details or wiki for FAQ
" Put your non-Plugin stuff after this line

" Sytax hilighting
" let g:syntastic_cpp_compiler = 'clang++'
" let g:syntastic_cpp_compiler_options = ' -std=c++11 -stdlib=libc++'
let g:ycm_confirm_extra_conf=0
" let g:ycm_extra_conf_globlist=1

" vim-session
let g:nerdtree_tabs_open_on_gui_startup=0  
let g:nerdtree_tabs_open_on_new_tab=0
let g:session_autoload = 'no'
let g:session_autosave = 'no'

let g:ConqueTerm_Color = 2         " 1: strip color after 200 lines, 2: always with color
let g:ConqueTerm_CloseOnEnd = 1    " close conque when program ends running
let g:ConqueTerm_StartMessages = 0 " display warning messages if conqueTerm is configured incorrectly
" indention levels hilighting
let g:indentLine_char = '┆'
" split settings
set splitbelow
set splitright
" bash-like completion
set wildmode=longest,list:longest
set wildmenu
" colorscheme desert
" colorscheme beekai
" colorscheme badwolf
colorscheme antares
hi Search guibg=background ctermbg=6
hi Search guifg=background ctermfg=black
hi ColorColumn ctermbg=233
let &colorcolumn=join(range(81,999),",")
" hilight line with the coursor
set cursorline
" nerd tree open on start with directory as an argument
autocmd StdinReadPre * let s:std_in=1
autocmd VimEnter * if argc() == 1 && isdirectory(argv()[0]) && !exists("s:std_in") | exe 'NERDTree' argv()[0] | wincmd p | ene | endif
" fixes error message
let g:ycm_global_ycm_extra_conf = '.vim/bundle/YouCompleteMe/third_party/ycmd/cpp/ycm/.ycm_extra_conf.py'
set omnifunc=syntaxcomplete#Complete
set number
"gm for 'generate mark' because m is used for move by easyclip
noremap gm m
" Add spaces after comment delimiters by default
let g:NERDSpaceDelims = 1
" Align line-wise comment delimiters flush left instead of following code indentation
let g:NERDDefaultAlign = 'left'
" Allow commenting and inverting empty lines (useful when commenting a region)
let g:NERDCommentEmptyLines = 1
" Enable trimming of trailing whitespace when uncommenting
let g:NERDTrimTrailingWhitespace = 1
" // for C
let g:NERDAltDelims_c = 1
" syntastic settings
set statusline+=%#warningmsg#
set statusline+=%{SyntasticStatuslineFlag()}
set statusline+=%*

let g:syntastic_always_populate_loc_list = 1
let g:syntastic_auto_loc_list = 1
let g:syntastic_check_on_open = 1
let g:syntastic_check_on_wq = 0


"" Last update: 23.03.2016 08:44
"" .vimrc  файл конфирурации для текстового редактора VIM
"" dimio (http://dimio.org)
"" Подробности по адресу:
"" http://dimio.org/fajl-nastrojki-vim-vimrc-dlya-linux-i-windows.html
"=====================================================================
"НАСТРОЙКИ ВНЕШНЕГО ВИДА И БАЗОВЫЕ НАСТРОЙКИ РЕДАКТОРА
set scrolloff=15 " сколько строк внизу и вверху экрана показывать при скроллинге
"set background=dark " установить цвет фона
"цветовая схема по умолчанию (при вводе в режиме команд
"по табуляции доступно автодополнение имён схем). af, desert
"set wrap " (no)wrap - динамический (не)перенос длинных строк
"set linebreak " переносить целые слова
set hidden " не выгружать буфер когда переключаешься на другой
" set mouse=a " включает поддержку мыши при работе в терминале (без GUI)
set mousehide " скрывать мышь в режиме ввода текста
set showcmd " показывать незавершенные команды в статусбаре (автодополнение ввода)
set mps+=<:> " показывать совпадающие скобки для HTML-тегов
set showmatch " показывать первую парную скобку после ввода второй
set autoread " перечитывать изменённые файлы автоматически
set t_Co=256 " использовать больше цветов в терминале
set confirm " использовать диалоги вместо сообщений об ошибках
"" Автоматически перечитывать конфигурацию VIM после сохранения
"autocmd! bufwritepost $MYVIMRC source $MYVIMRC
"" Формат строки состояния
" fileformat - формат файла (unix, dos); fileencoding - кодировка файла;
" encoding - кодировка терминала; TYPE - тип файла, затем коды символа под курсором;
" позиция курсора (строка, символ в строке); процент прочитанного в файле;
" кол-во строк в файле;
"set statusline=%F%m%r%h%w\ [FF,FE,TE=%{&fileformat},%{&fileencoding},%{&encoding}\]\ [TYPE=%Y]\ [ASCII=\%03.3b]\ [HEX=\%02.2B]\ [POS=%04l,%04v][%p%%]\ [LEN=%L]
"Изменяет шрифт строки статуса (делает его не жирным)
hi StatusLine gui=reverse cterm=reverse
set laststatus=2 " всегда показывать строку состояния
"set noruler "Отключить линейку
"" Подсвечивать табы и пробелы в конце строки
set list " включить подсветку
set listchars=tab:>-,trail:- " установить символы, которыми будет осуществляться подсветка
"Проблема красного на красном при spellchecking-е решается такой строкой в .vimrc
highlight SpellBad ctermfg=Black ctermbg=Red
au BufWinLeave *.* silent mkview " при закрытии файла сохранить 'вид'
au BufWinEnter *.* silent loadview " при открытии - восстановить сохранённый
set backspace=indent,eol,start " backspace обрабатывает отступы, концы строк
set sessionoptions=curdir,buffers,tabpages " resize,winpos,winsize " опции сессий - перейти в текущую директорию, использовать буферы и табы
set noswapfile " не использовать своп-файл (в него скидываются открытые буферы)
set browsedir=current
"set visualbell " вместо писка бипером мигать курсором при ошибках ввода
set clipboard=unnamed " во избежание лишней путаницы использовать системный буфер обмена вместо буфера Vim
set backup " включить сохранение резервных копий
autocmd! bufwritepre * call BackupDir() " сделаем резервную копию перед записью буфера на диск
set title " показывать имя буфера в заголовке терминала
set history=128 " хранить больше истории команд
set undolevels=2048 " хранить историю изменений числом N
"set whichwrap=b,<,>,[,],l,h " перемещать курсор на следующую строку при нажатии на клавиши вправо-влево и пр.
"set virtualedit=all " позволяет курсору выходить за пределы строки
"let c_syntax_for_h="" " необходимо установить для того, чтобы *.h файлам присваивался тип c, а не cpp
" При вставке фрагмента сохраняет отступ
" set pastetoggle=
"подсвечивает все слова, которые совпадают со словом под курсором.
autocmd CursorMoved * silent! exe printf("match Search /\\<%s\\>/", expand('<cword>'))


"НАСТРОЙКИ ПЕРЕМЕННЫХ ОКРУЖЕНИЯ
if has('win32')
   let $VIMRUNTIME = $HOME.'\Programs\Vim\vim72'
   source $VIMRUNTIME/mswin.vim
else
   let $VIMRUNTIME = $HOME.'/.vim'
endif

"НАСТРОЙКИ ПОИСКА ТЕКСТА В ОТКРЫТЫХ ФАЙЛАХ
set ignorecase " ics - поиск без учёта регистра символов
set smartcase " - если искомое выражения содержит символы в верхнем регистре - ищет с учётом регистра, иначе - без учёта
set nohlsearch " (не)подсветка результатов поиска (после того, как поиск закончен и закрыт)
set incsearch " поиск фрагмента по мере его набора
" поиск выделенного текста (начинать искать фрагмент при его выделении)
vnoremap <silent>* <ESC>:call VisualSearch()<CR>/<C-R>/<CR>
vnoremap <silent># <ESC>:call VisualSearch()<CR>?<C-R>/<CR>


"НАСТРОЙКИ СВОРАЧИВАНИЯ БЛОКОВ ТЕКСТА (фолдинг)
set foldenable " включить фолдинг
"set foldmethod=syntax " определять блоки на основе синтаксиса файла
set foldmethod=indent " определять блоки на основе отступов
set foldcolumn=3 " показать полосу для управления сворачиванием
set foldlevel=1 " Первый уровень вложенности открыт, остальные закрыты
"set foldopen=all " автоматическое открытие сверток при заходе в них
set tags=tags\ $VIMRUNTIME/systags " искать теги в текущй директории и в указанной (теги генерируются ctags)
nnoremap <silent> <Space> @=(foldlevel('.')?'za':"\<Space>")<CR>
vnoremap <Space> zf
"inoremap <F9> <C-O>za
"nnoremap <F9> za
"onoremap <F9> <C-C>za
"vnoremap <F9> zf

"НАСТРОЙКИ РАБОТЫ С ФАЙЛАМИ
"Кодировка редактора (терминала) по умолчанию (при создании все файлы приводятся к этой кодировке)
"if has('win32')
"   set encoding=cp1251
"else
"   set encoding=utf-8
"   set termencoding=utf-8
"endif
" формат файла по умолчанию (влияет на окончания строк) - будет перебираться в указанном порядке
set fileformat=unix
" варианты кодировки файла по умолчанию (все файлы по умолчанию сохраняются в этой кодировке)
set fencs=utf-8,cp1251,koi8-r,cp866
"" Перед сохранением .vimrc обновлять дату последнего изменения
autocmd! bufwritepre $MYVIMRC call setline(1, '"" Last update: '.strftime("%d.%m.%Y %H:%M"))
"syntax on " включить подсветку синтаксиса
"" Применять типы файлов
filetype on
filetype plugin on
filetype indent on
" autocmd FileType perl call SetPerlConf()
"Удалять пустые пробелы на концах строк при открытии файла
" autocmd BufEnter *.* :call RemoveTrailingSpaces()
"Путь для поиска файлов командами gf, [f, ]f, ^Wf, :find, :sfind, :tabfind и т.д.
"поиск начинается от директории текущего открытого файла, ищет в ней же
"и в поддиректориях. Пути для поиска перечисляются через запятую, например:
"set path=.,,**,/src,/usr/local
set path=.,,**


"НАСТРОЙКИ ОТСТУПА
set shiftwidth=2 " размер отступов (нажатие на << или >>)
set tabstop=2 " ширина табуляции
set softtabstop=2 " ширина 'мягкого' таба
set autoindent " ai - включить автоотступы (копируется отступ предыдущей строки)
set cindent " ci - отступы в стиле С
set expandtab " преобразовать табуляцию в пробелы
set smartindent " Умные отступы (например, автоотступ после {)
" Для указанных типов файлов отключает замену табов пробелами и меняет ширину отступа
au FileType crontab,fstab,make set noexpandtab tabstop=8 shiftwidth=8


"НАСТРОЙКИ ВНЕШНЕГО ВИДА
" Установка шрифта (для Windows и Linux)
" настройка внешнего вида для GUI
if has('gui')
    " отключаем графические табы (останутся текстовые,
    " занимают меньше места на экране)
    set guioptions-=e
    " отключить показ иконок в окне GUI (файл, сохранить и т.д.)
    set guioptions-=T

    if has('win32')
        set guifont=Lucida_Console:h10:cRUSSIAN::
    else
        set guifont=Terminus\ 10
    endif
endif


"НАСТРОЙКИ ПЕРЕКЛЮЧЕНИЯ РАСКЛАДОК КЛАВИАТУРЫ
"" Взято у konishchevdmitry
set keymap=russian-jcukenwin " настраиваем переключение раскладок клавиатуры по <C-^>
set iminsert=0 " раскладка по умолчанию - английская
set imsearch=0 " аналогично для строки поиска и ввода команд
function! MyKeyMapHighlight()
   if &iminsert == 0 " при английской раскладке статусная строка текущего окна будет серого цвета
      hi StatusLine ctermfg=White guifg=White
   else " а при русской - зеленого.
      hi StatusLine ctermfg=DarkRed guifg=DarkRed
   endif
endfunction
" call MyKeyMapHighlight() " при старте Vim устанавливать цвет статусной строки
autocmd WinEnter * :call MyKeyMapHighlight() " при смене окна обновлять информацию о раскладках
" использовать Ctrl+F для переключения раскладок
" cmap <silent> <C-q> <C-^>
" cmap <silent> <C-F> <C-^>
" imap <silent> <C-q> <C-^>X<Esc>:call MyKeyMapHighlight()<CR>a<C-H>
" imap <silent> <C-F> <C-^>X<Esc>:call MyKeyMapHighlight()<CR>a<C-H>
" nmap <silent> <C-q> a<C-^><Esc>:call MyKeyMapHighlight()<CR>
" nmap <silent> <C-F> a<C-^><Esc>:call MyKeyMapHighlight()<CR>
" vmap <silent> <C-q> <Esc>a<C-^><Esc>:call MyKeyMapHighlight()<CR>gv
" vmap <silent> <C-F> <Esc>a<C-^><Esc>:call MyKeyMapHighlight()<CR>gv

"НАСТРОЙКИ ГОРЯЧИХ КЛАВИШ
" Disable Arrow keys in Insert mode
imap <up> <nop>
imap <down> <nop>
imap <left> <nop>
imap <right> <nop>
" Disable Arrow keys in Escape mode
map <up> <nop>
map <down> <nop>
map <left> <nop>
map <right> <nop>
" close buffer
nnoremap <F4> :bd<cr>
" split navigation
nnoremap <C-J> <C-W><C-J>
nnoremap <C-K> <C-W><C-K>
nnoremap <C-L> <C-W><C-L>
nnoremap <C-H> <C-W><C-H>
" Swap line with above (Uses easyClip)
map <S-k> mmkP
" Swap line with below
map <S-j> mmp
" F3 - рекурсивный поиск по файлам (плагин grep.vim)
nnoremap <silent> <F3> :Rgrep<cr>
" Shift-F<F3> - добавление найденного к прошлым результатам поиска
nnoremap <silent> <S-F<F3>> :RgrepAdd<cr>
" Ctrl-F<F3> - поиск в открытых буферах
nnoremap <silent> <C-F<F3>> :GrepBuffer<cr>
" при включенном плагине можно использовать его
nmap <F3> :BufExplorer<CR>
" F6 - предыдущий буфер
map <F1> :bp<cr>
vmap <F1> <esc>:bp<cr>i
imap <F1> <esc>:bp<cr>i
" F7 - следующий буфер
map <F2> :bn<cr>
vmap <F2> <esc>:bn<cr>i
imap <F2> <esc>:bn<cr>i
" F9 - сохранение файла и запуск компиляции (make)
map <F9> :w<cr>:make<cr>
vmap <F9> <esc>:w<cr>:make<cr>i
imap <F9> <esc>:w<cr>:make<cr>i
" F10 - включить-выключить браузер структуры документа (TagList)
map <F10> :TlistToggle<cr>
vmap <F10> <esc>:TlistToggle<cr>
imap <F10> <esc>:TlistToggle<cr>
" F12 - обозреватель файлов (:Ex для стандартного обозревателя,
" плагин NERDTree - дерево каталогов)
map <F12> :NERDTreeToggle<cr>
vmap <F12> <esc>:NERDTreeToggle<cr>i
imap <F12> <esc>:NERDTreeToggle<cr>i
"" Переключение табов (вкладок) (rxvt-style)
"map <S-left> :tabprevious<cr>
"nmap <S-left> :tabprevious<cr>
"imap <S-left> <ESC>:tabprevious<cr>i
"map <S-right> :tabnext<cr>
"nmap <S-right> :tabnext<cr>
"imap <S-right> <ESC>:tabnext<cr>i
"nmap <C-t> :tabnew<cr>
"imap <C-t> <ESC>:tabnew<cr>
"nmap <S-down> :tabnew<cr>
"imap <S-down> <ESC>:tabnew<cr>
"nmap <C-w> :tabclose<cr>
"imap <C-w> <ESC>:tabclose<cr>
" map <C-h> :tabprevious<cr>
" nmap <C-h> :tabprevious<cr>
" imap <C-h> <ESC>:tabprevious<cr>i
" map <C-l> :tabnext<cr>
" nmap <C-l> :tabnext<cr>
" imap <C-l> <ESC>:tabnext<cr>i
" nmap <C-n> :tabnew<cr>
" imap <C-n> <ESC>:tabnew<cr>
" nmap <C-w> :tabclose<cr>
" imap <C-w> <ESC>:tabclose<cr>

"" Переключение кодировок файла
   " Меню Encoding -->
        " Выбор кодировки, в которой читать файл -->
            set wildmenu
            set wcm=<Tab>
            menu Encoding.Read.utf-8<Tab><F7> :e ++enc=utf8 <CR>
            menu Encoding.Read.windows-1251<Tab><F7> :e ++enc=cp1251<CR>
            menu Encoding.Read.koi8-r<Tab><F7> :e ++enc=koi8-r<CR>
            menu Encoding.Read.cp866<Tab><F7> :e ++enc=cp866<CR>
            map <F8> :emenu Encoding.Read.<TAB>
        " Выбор кодировки, в которой читать файл <--

        " Выбор кодировки, в которой сохранять файл -->
            set wildmenu
            set wcm=<Tab>
            menu Encoding.Write.utf-8<Tab><S-F7> :set fenc=utf8 <CR>
            menu Encoding.Write.windows-1251<Tab><S-F7> :set fenc=cp1251<CR>
            menu Encoding.Write.koi8-r<Tab><S-F7> :set fenc=koi8-r<CR>
            menu Encoding.Write.cp866<Tab><S-F7> :set fenc=cp866<CR>
            map <S-F7> :emenu Encoding.Write.<TAB>
        " Выбор кодировки, в которой сохранять файл <--

        " Выбор формата концов строк (dos - <CR><NL>, unix - <NL>, mac - <CR>) -->
            set wildmenu
            set wcm=<Tab>
            menu Encoding.End_line_format.unix<Tab><C-F7> :set fileformat=unix<CR>
            menu Encoding.End_line_format.dos<Tab><C-F7> :set fileformat=dos<CR>
            menu Encoding.End_line_format.mac<Tab><C-F7> :set fileformat=mac<CR>
            map <C-F7> :emenu Encoding.End_line_format.<TAB>
        " Выбор формата концов строк (dos - <CR><NL>, unix - <NL>, mac - <CR>) <--
    " Меню Encoding <--

    " Включение автоматического разбиения строки на несколько
    " строк фиксированной длины
   " menu Textwidth.off :set textwidth=0<CR>
   " menu Textwidth.on :set textwidth=78<CR>
    " Проверка орфографии -->
        if version >= 700
            " По умолчанию проверка орфографии выключена.
            set spell spelllang=
            set nospell
            menu Spell.off :setlocal spell spelllang=<CR>:setlocal nospell<CR>
            menu Spell.Russian+English :setlocal spell spelllang=ru,en<CR>
            menu Spell.Russian :setlocal spell spelllang=ru<CR>
            menu Spell.English :setlocal spell spelllang=en<CR>
            menu Spell.-SpellControl- :
            menu Spell.Word\ Suggest<Tab>z= z=
            menu Spell.Add\ To\ Dictionary<Tab>zg zg
            menu Spell.Add\ To\ TemporaryDictionary<Tab>zG zG
            menu Spell.Remove\ From\ Dictionary<Tab>zw zw
            menu Spell.Remove\ From\ Temporary\ Dictionary<Tab>zW zW
            menu Spell.Previous\ Wrong\ Word<Tab>[s [s
            menu Spell.Next\ Wrong\ Word<Tab>]s ]s
        endif
    " Проверка орфографии <--

    " Обертка для :make -->
        nmap ,m :call make<CR>
        nmap ,w :cwindow<CR>
        nmap ,n :cnext<CR>
        nmap ,p :cprevious<CR>
        nmap ,l :clist<CR>

        menu Make.Make<Tab>,m ,m
        menu Make.Make\ Window<Tab>,w ,w
        menu Make.Next\ Error<Tab>,n ,n
        menu Make.Previous\ Error<Tab>,p ,p
        menu Make.Errors\ List<Tab>,l ,l
    " Обертка для :make <--

    " Обновление ctags -->
        function! MyUpdateCtags()
            echo "Update ctags function is not setted."
        endfunction
        let MyUpdateCtagsFunction = "MyUpdateCtags"
        nmap <F5> :call {MyUpdateCtagsFunction}()<CR>
        menu ctags.Update<Tab><F5> <F5>
    " Обновление ctags <--

" C(trl)+d - дублирование текущей строки
" imap <C-d> <esc>yypi
" Ctrl-пробел для автодополнения
" inoremap <C-space> <C-x><C-o>
" C-e - комментировать/раскомментировать (при помощи NERD_Comment)
map <C-e> \cij
nmap <C-e> \cij
imap <C-e> <ESC>\ciij


"ФУНКЦИИ (не вошедшие в состав других разделов)
" Применение дополнительных настроек при открытии perl-файла
" При открытии файла задавать для него соответствующий 'компилятор'
" и настроечный файл для IDE.
" Установить метод свертки блоков кода по отступам
function! SetPerlConf()
    compiler perl
    "" source "$VIMRUNTIME/IDE/perl-ide.vim"
    set foldmethod=indent
    " настройка плагина подсветки синтаксиса для Mojolicious
    " github.com/yko/mojo.vim
    " подсвечивать perl-код в секции __DATA__ perl-файлов
    let mojo_highlight_data = 1
endfunction

"" Поиск выделенного текста (frantsev.ru/configs/vimrc.txt)
function! VisualSearch()
   let l:old_reg=getreg('"')
   let l:old_regtype=getregtype('"')
   normal! gvy
   let @/=escape(@@, '$.*/\[]')
   normal! gV
   call setreg('"', l:old_reg, l:old_regtype)
endfunction

"" Удалить пробелы в конце строк (frantsev)
function! RemoveTrailingSpaces()
   normal! mzHmy
   execute '%s:\s\+$::ge'
   normal! 'yzt`z
endfunction

"" Сохранять умные резервные копии ежедневно
function! BackupDir()
   " определим каталог для сохранения резервной копии
   if has('win32')
        let l:backupdir = $TEMP.'\backup'
    else
        let l:backupdir = $VIMRUNTIME.'/backup/'.
        \substitute(expand('%:p:h'), '^'.$HOME, '~', '')
    endif
   " если каталог не существует, создадим его рекурсивно
   if !isdirectory(l:backupdir)
      call mkdir(l:backupdir, 'p', 0700)
   endif
   " переопределим каталог для резервных копий
   let &backupdir=l:backupdir
   " переопределим расширение файла резервной копии
   let &backupext=strftime('~%Y-%m-%d~')
endfunction

" nnoremap <F8> :colo random <Enter>
