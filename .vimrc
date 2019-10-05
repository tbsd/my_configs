set nocompatible              " be iMproved, required
filetype off                  " required
" set the runtime path to include Vundle and initialize
set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()
" alternatively, pass a path where Vundle should install plugins
"call vundle#begin('~/some/path/here')
" let Vundle manage Vundle, required
Plugin 'VundleVim/Vundle.vim'
Plugin 'ycm-core/YouCompleteMe'
Plugin 'dhruvasagar/vim-table-mode'
Plugin 'tpope/vim-repeat'
Plugin 'scrooloose/nerdcommenter'
Plugin 'flazz/vim-colorschemes'
Plugin 'jacquesbh/vim-showmarks'
" Plugin 'w0rp/ale' "syntax chekcer
" Plugin 'SirVer/ultisnips'
Plugin 'jiangmiao/auto-pairs'
Plugin 'Yggdroot/indentLine'
Plugin 'Lokaltog/vim-powerline'
Plugin 'scrooloose/nerdtree'
Plugin 'xolox/vim-misc'
Plugin 'xolox/vim-session'
" All of your Plugins must be added before the following line
call vundle#end()            " required
"filetype plugin indent on    " required
" To ignore plugin indent changes, instead use:
filetype plugin on

" CUSTOM SETTINGS
" split settings
set splitbelow
" set splitright
" bash-like completion
set wildmode=longest,list:longest
" set wildmenu
" color scheme and other color stuff
colorscheme antares
"Проблема красного на красном при spellchecking-е решается такой строкой в .vimrc
highlight SpellBad ctermfg=Black ctermbg=Red
"" Подсвечивать табы и пробелы в конце строки
set list " включить подсветку
set listchars=tab:>-,trail:- " установить символы, которыми будет осуществляться подсветка
" hilight line with the coursor
set cursorline
hi Search guibg=background ctermbg=6
hi Search guifg=background ctermfg=black
hi ColorColumn ctermbg=233
let &colorcolumn=join(range(81,999),",")
syntax on " включить подсветку синтаксиса
" shift
set shiftwidth=2 " размер отступов (нажатие на << или >>)
set tabstop=2 " ширина табуляции
set autoindent " ai - включить автоотступы (копируется отступ предыдущей строки)
"set expandtab " преобразовать табуляцию в пробелы
set smartindent " Умные отступы (например, автоотступ после {)
" формат файла по умолчанию (влияет на окончания строк) - будет перебираться в указанном порядке
set fileformat=unix
" search
set ignorecase " ics - поиск без учёта регистра символов
set smartcase " - если искомое выражения содержит символы в верхнем регистре - ищет с учётом регистра, иначе - без учёта
set nohlsearch " (не)подсветка результатов поиска (после того, как поиск закончен и закрыт)
set incsearch " поиск фрагмента по мере его набора
" status line
" Изменяет шрифт строки статуса (делает его не жирным)
hi StatusLine gui=reverse cterm=reverse
set laststatus=2 " всегда показывать строку состояния
" different stuff
filetype on
filetype plugin on
filetype indent on
set scrolloff=15 " сколько строк внизу и вверху экрана показывать при скроллинге
set hidden " не выгружать буфер когда переключаешься на другой
set mousehide " скрывать мышь в режиме ввода текста
set showcmd " показывать незавершенные команды в статусбаре (автодополнение ввода)
set mps+=<:> " показывать совпадающие скобки для HTML-тегов
set showmatch " показывать первую парную скобку после ввода второй
set autoread " перечитывать изменённые файлы автоматически
" set t_Co=256 " использовать больше цветов в терминале
" set confirm " использовать диалоги вместо сообщений об ошибках
au BufWinLeave *.* silent mkview " при закрытии файла сохранить 'вид'
au BufWinEnter *.* silent loadview " при открытии - восстановить сохранённый
set noswapfile " не использовать своп-файл (в него скидываются открытые буферы)
" set browsedir=current
set backup " включить сохранение резервных копий
set title " показывать имя буфера в заголовке терминала
" подсвечивает все слова, которые совпадают со словом под курсором.
autocmd CursorMoved * silent! exe printf("match Search /\\<%s\\>/", expand('<cword>'))
"Save marks through restarts
set viminfo='1000,f1
" turn hybrid line numbers on
set number
set relativenumber
" Sytax hilighting
let g:ycm_confirm_extra_conf=0

" Hotkeys
" close buffer
nnoremap <F4> :bd<cr>
" split navigation
nnoremap <C-J> <C-W><C-J>
nnoremap <C-K> <C-W><C-K>
nnoremap <C-L> <C-W><C-L>
nnoremap <C-H> <C-W><C-H>
" предыдущий буфер
map <F1> :bp<cr>
vmap <F1> <esc>:bp<cr>i
imap <F1> <esc>:bp<cr>i
" следующий буфер
map <F2> :bn<cr>
vmap <F2> <esc>:bn<cr>i
imap <F2> <esc>:bn<cr>i
" nordetree обозреватель файлов
map <F12> :NERDTreeToggle<cr>
vmap <F12> <esc>:NERDTreeToggle<cr>i
imap <F12> <esc>:NERDTreeToggle<cr>i
" C-e - комментировать/раскомментировать (при помощи NERD_Comment)
map <C-e> \cij
nmap <C-e> \cij
imap <C-e> <ESC>\ciij


" PLUGINS SETTINGS
" vim-showmarks
let g:showmarks_marks="qwertyuiop[]\\asdfghjkl;'zxcbnm,./QWERTYUIOP{}\":LKJHGFDSAZXCVBNM<>?1234567890-=`~!@#$%^&*()_+"
set updatetime=100
autocmd VimEnter * DoShowMarks
let g:indentLine_char = '┆'
" vim-table-mode
autocmd VimEnter * TableModeEnable
let g:table_mode_motion_up_map = '<leader>j'
let g:table_mode_motion_down_map = '<leader>j'
let g:table_mode_motion_left_map = '<leader>h'
let g:table_mode_motion_right_map = '<leader>l'
" nerdtree
let g:nerdtree_tabs_open_on_gui_startup=0  
let g:nerdtree_tabs_open_on_new_tab=0
let g:session_autoload = 'no'
let g:session_autosave = 'no'
" nerdcomment
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
" nerd tree open on start with directory as an argument
autocmd StdinReadPre * let s:std_in=1
autocmd VimEnter * if argc() == 1 && isdirectory(argv()[0]) && !exists("s:std_in") | exe 'NERDTree' argv()[0] | wincmd p | ene | endif
